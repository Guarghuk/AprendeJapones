/**
 * MainActivity is the entry point of the application.
 * It handles the initialization of the app and sets the content view.
 *
 * @constructor Creates an instance of MainActivity.
 * @see Activity
 */
class MainActivity : AppCompatActivity() {

    /**
     * Called when the activity is first created.
     * This is where the application logic is initialized.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Sets the layout for the main activity
    }

    /**
     * This method is called when the activity is resumed.
     * You can use it to refresh the UI or data when the user returns to this activity.
     */
    override fun onResume() {
        super.onResume()
        // Refresh the UI or data here
    }
}