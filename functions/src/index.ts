/**
 * Cloud Functions for AprendeJapones
 * 
 * Handles automatic counter updates for:
 * - contador_comentarios: Incremented/decremented when comments are created/deleted
 * - contador_likes: Incremented/decremented when likes are created/deleted
 */

import * as functions from "firebase-functions";
import * as admin from "firebase-admin";

admin.initializeApp();

const db = admin.firestore();

/**
 * Triggered when a comment is created in /posts/{postId}/comments/{commentId}
 * Increments contador_comentarios on the parent post
 */
export const onCommentCreated = functions.firestore
  .document("posts/{postId}/comments/{commentId}")
  .onCreate(async (snapshot, context) => {
    const postId = context.params.postId;
    
    try {
      const postRef = db.collection("posts").doc(postId);
      
      await postRef.update({
        contador_comentarios: admin.firestore.FieldValue.increment(1),
      });
      
      functions.logger.info(`Incremented contador_comentarios for post ${postId}`);
    } catch (error) {
      functions.logger.error(`Error incrementing contador_comentarios for post ${postId}:`, error);
    }
  });

/**
 * Triggered when a comment is deleted from /posts/{postId}/comments/{commentId}
 * Decrements contador_comentarios on the parent post
 */
export const onCommentDeleted = functions.firestore
  .document("posts/{postId}/comments/{commentId}")
  .onDelete(async (snapshot, context) => {
    const postId = context.params.postId;
    
    try {
      const postRef = db.collection("posts").doc(postId);
      
      // Use a transaction to ensure contador doesn't go below 0
      await db.runTransaction(async (transaction) => {
        const postDoc = await transaction.get(postRef);
        
        if (!postDoc.exists) {
          functions.logger.warn(`Post ${postId} not found when decrementing contador_comentarios`);
          return;
        }
        
        const currentCount = postDoc.data()?.contador_comentarios || 0;
        const newCount = Math.max(0, currentCount - 1);
        
        transaction.update(postRef, {
          contador_comentarios: newCount,
        });
      });
      
      functions.logger.info(`Decremented contador_comentarios for post ${postId}`);
    } catch (error) {
      functions.logger.error(`Error decrementing contador_comentarios for post ${postId}:`, error);
    }
  });

/**
 * Triggered when a like is created in /posts/{postId}/likes/{userId}
 * Increments contador_likes on the parent post
 */
export const onLikeCreated = functions.firestore
  .document("posts/{postId}/likes/{userId}")
  .onCreate(async (snapshot, context) => {
    const postId = context.params.postId;
    
    try {
      const postRef = db.collection("posts").doc(postId);
      
      await postRef.update({
        contador_likes: admin.firestore.FieldValue.increment(1),
      });
      
      functions.logger.info(`Incremented contador_likes for post ${postId}`);
    } catch (error) {
      functions.logger.error(`Error incrementing contador_likes for post ${postId}:`, error);
    }
  });

/**
 * Triggered when a like is deleted from /posts/{postId}/likes/{userId}
 * Decrements contador_likes on the parent post
 */
export const onLikeDeleted = functions.firestore
  .document("posts/{postId}/likes/{userId}")
  .onDelete(async (snapshot, context) => {
    const postId = context.params.postId;
    
    try {
      const postRef = db.collection("posts").doc(postId);
      
      // Use a transaction to ensure contador doesn't go below 0
      await db.runTransaction(async (transaction) => {
        const postDoc = await transaction.get(postRef);
        
        if (!postDoc.exists) {
          functions.logger.warn(`Post ${postId} not found when decrementing contador_likes`);
          return;
        }
        
        const currentCount = postDoc.data()?.contador_likes || 0;
        const newCount = Math.max(0, currentCount - 1);
        
        transaction.update(postRef, {
          contador_likes: newCount,
        });
      });
      
      functions.logger.info(`Decremented contador_likes for post ${postId}`);
    } catch (error) {
      functions.logger.error(`Error decrementing contador_likes for post ${postId}:`, error);
    }
  });

/**
 * Optional: Triggered when a user account is created in Firebase Auth
 * Creates an initial user profile in Firestore
 * 
 * Note: This function provides a server-side way to ensure user profiles exist.
 * The client also creates profiles, so this serves as a backup mechanism.
 */
export const onUserCreated = functions.auth.user().onCreate(async (user) => {
  const userId = user.uid;
  const email = user.email || "";
  const displayName = user.displayName || email.split("@")[0] || "Usuario";
  
  try {
    const userRef = db.collection("users").doc(userId);
    const userDoc = await userRef.get();
    
    // Only create if the profile doesn't already exist (client may have created it)
    if (!userDoc.exists) {
      await userRef.set({
        id_usuario: userId,
        nombre_usuario: displayName,
        email: email,
        foto_url: user.photoURL || null,
        rango: "初心者",
        nivel: 1,
        xp: 0,
        racha: 0,
        gotas: 0,
        biografia: null,
        fecha_registro: admin.firestore.FieldValue.serverTimestamp(),
      });
      
      functions.logger.info(`Created user profile for ${userId}`);
    }
  } catch (error) {
    functions.logger.error(`Error creating user profile for ${userId}:`, error);
  }
});

/**
 * Optional: Add XP to a user (can be called from client via callable function)
 * This is more secure than allowing clients to update XP directly
 */
export const addXpToUser = functions.https.onCall(async (data, context) => {
  // Verify authentication
  if (!context.auth) {
    throw new functions.https.HttpsError(
      "unauthenticated",
      "User must be authenticated to add XP"
    );
  }
  
  const userId = context.auth.uid;
  const xpToAdd = data.xp;
  
  // Validate input
  if (typeof xpToAdd !== "number" || xpToAdd < 0 || xpToAdd > 1000) {
    throw new functions.https.HttpsError(
      "invalid-argument",
      "XP must be a number between 0 and 1000"
    );
  }
  
  try {
    const userRef = db.collection("users").doc(userId);
    
    const result = await db.runTransaction(async (transaction) => {
      const userDoc = await transaction.get(userRef);
      
      if (!userDoc.exists) {
        throw new functions.https.HttpsError(
          "not-found",
          "User profile not found"
        );
      }
      
      const userData = userDoc.data();
      const currentXp = userData?.xp || 0;
      const currentLevel = userData?.nivel || 1;
      
      let newXp = currentXp + xpToAdd;
      let newLevel = currentLevel;
      
      // Level up logic (100 XP per level)
      while (newXp >= 100) {
        newLevel++;
        newXp -= 100;
      }
      
      // Determine new rank based on level
      let newRank = "初心者"; // Beginner
      if (newLevel >= 50) {
        newRank = "達人"; // Master
      } else if (newLevel >= 30) {
        newRank = "上級者"; // Advanced
      } else if (newLevel >= 15) {
        newRank = "中級者"; // Intermediate
      } else if (newLevel >= 5) {
        newRank = "見習い"; // Apprentice
      }
      
      transaction.update(userRef, {
        xp: newXp,
        nivel: newLevel,
        rango: newRank,
      });
      
      return {
        newXp,
        newLevel,
        newRank,
        leveledUp: newLevel > currentLevel,
      };
    });
    
    return result;
  } catch (error) {
    functions.logger.error(`Error adding XP to user ${userId}:`, error);
    throw new functions.https.HttpsError(
      "internal",
      "Error adding XP"
    );
  }
});
