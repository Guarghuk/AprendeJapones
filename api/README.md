# API de Registro de Estudiantes

API REST construida con **Flask** y **SQLite** que permite registrar y consultar estudiantes.

## Requisitos

- Python 3.9 o superior

## Instalación

```bash
cd api
python -m venv venv
source venv/bin/activate   # En Windows: venv\Scripts\activate
pip install -r requirements.txt
```

## Ejecución

```bash
cd api
source venv/bin/activate   # En Windows: venv\Scripts\activate
python app.py
```

El servidor se inicia en `http://127.0.0.1:5000`.

## Endpoints

### POST /estudiantes

Registra un nuevo estudiante.

**Cuerpo (JSON):**

```json
{
  "nombre": "Ana López",
  "carrera": "Ingeniería en Sistemas",
  "semestre": 3
}
```

**Ejemplo con curl:**

```bash
curl -X POST http://127.0.0.1:5000/estudiantes \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Ana López", "carrera": "Ingeniería en Sistemas", "semestre": 3}'
```

**Respuesta exitosa (201):**

```json
{
  "mensaje": "Estudiante registrado exitosamente",
  "estudiante": {
    "id": 1,
    "nombre": "Ana López",
    "carrera": "Ingeniería en Sistemas",
    "semestre": 3
  }
}
```

### GET /estudiantes

Devuelve todos los estudiantes registrados.

**Ejemplo con curl:**

```bash
curl http://127.0.0.1:5000/estudiantes
```

**Respuesta (200):**

```json
[
  {
    "id": 1,
    "nombre": "Ana López",
    "carrera": "Ingeniería en Sistemas",
    "semestre": 3
  }
]
```

## Pruebas

```bash
cd api
source venv/bin/activate
pip install pytest
pytest test_app.py -v
```
