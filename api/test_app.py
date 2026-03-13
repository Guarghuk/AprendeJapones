import pytest

from app import app, db


@pytest.fixture()
def client():
    app.config["TESTING"] = True
    app.config["SQLALCHEMY_DATABASE_URI"] = "sqlite:///:memory:"
    with app.app_context():
        db.create_all()
        yield app.test_client()
        db.drop_all()


def test_get_estudiantes_vacio(client):
    resp = client.get("/estudiantes")
    assert resp.status_code == 200
    assert resp.get_json() == []


def test_post_estudiante(client):
    payload = {"nombre": "Ana López", "carrera": "Ingeniería", "semestre": 3}
    resp = client.post("/estudiantes", json=payload)
    assert resp.status_code == 201
    data = resp.get_json()
    assert data["estudiante"]["nombre"] == "Ana López"
    assert data["estudiante"]["carrera"] == "Ingeniería"
    assert data["estudiante"]["semestre"] == 3
    assert "id" in data["estudiante"]


def test_get_estudiantes_despues_de_crear(client):
    client.post("/estudiantes", json={"nombre": "Carlos", "carrera": "Derecho", "semestre": 1})
    client.post("/estudiantes", json={"nombre": "María", "carrera": "Medicina", "semestre": 5})
    resp = client.get("/estudiantes")
    assert resp.status_code == 200
    data = resp.get_json()
    assert len(data) == 2
    nombres = [e["nombre"] for e in data]
    assert "Carlos" in nombres
    assert "María" in nombres


def test_post_sin_json(client):
    resp = client.post("/estudiantes", content_type="application/json")
    assert resp.status_code == 400


def test_post_campos_faltantes(client):
    resp = client.post("/estudiantes", json={"nombre": "Test"})
    assert resp.status_code == 400
    assert "obligatorios" in resp.get_json()["error"]


def test_post_semestre_invalido(client):
    resp = client.post("/estudiantes", json={"nombre": "Test", "carrera": "X", "semestre": -1})
    assert resp.status_code == 400
    assert "positivo" in resp.get_json()["error"]
