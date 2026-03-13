from flask import Flask, request, jsonify
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
app.config["SQLALCHEMY_DATABASE_URI"] = "sqlite:///estudiantes.db"
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False

db = SQLAlchemy(app)


class Estudiante(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    nombre = db.Column(db.String(100), nullable=False)
    carrera = db.Column(db.String(100), nullable=False)
    semestre = db.Column(db.Integer, nullable=False)


with app.app_context():
    db.create_all()


@app.route("/estudiantes", methods=["POST"])
def crear_estudiante():
    datos = request.get_json()

    if not datos:
        return jsonify({"error": "Se requiere un cuerpo JSON"}), 400

    nombre = datos.get("nombre")
    carrera = datos.get("carrera")
    semestre = datos.get("semestre")

    if not nombre or not carrera or semestre is None:
        return jsonify({"error": "Los campos nombre, carrera y semestre son obligatorios"}), 400

    if not isinstance(semestre, int) or semestre < 1:
        return jsonify({"error": "El semestre debe ser un número entero positivo"}), 400

    nuevo = Estudiante(nombre=nombre, carrera=carrera, semestre=semestre)
    db.session.add(nuevo)
    db.session.commit()

    return jsonify({
        "mensaje": "Estudiante registrado exitosamente",
        "estudiante": {
            "id": nuevo.id,
            "nombre": nuevo.nombre,
            "carrera": nuevo.carrera,
            "semestre": nuevo.semestre,
        },
    }), 201


@app.route("/estudiantes", methods=["GET"])
def obtener_estudiantes():
    estudiantes = Estudiante.query.all()
    resultado = [
        {
            "id": e.id,
            "nombre": e.nombre,
            "carrera": e.carrera,
            "semestre": e.semestre,
        }
        for e in estudiantes
    ]
    return jsonify(resultado), 200


if __name__ == "__main__":
    app.run(debug=True)
