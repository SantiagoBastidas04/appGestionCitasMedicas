# ADR-001: Corrección Documentacion — Patrón Repository vs Template Method

**Fecha:** 2026-04-08  
**Estado:** Terminado

---

## Contexto

Se identificó una deuda de documentacion en el proyecto:

El documento de arquitectura afirmaba que la capa de persistencia implementaba el patrón de diseño **Template Method** para estructurar las operaciones de acceso a datos.

Sin embargo, al revisar el código fuente del repositorio, se encontró que los repositorios del proyecto (como `CitaRepository`, `MedicoTerapistaRepository`, `UsuarioRepository` y `ConfiguracionSistemaRepository`) donde realmente no se esta haciendo uso de l patron template. Realmente los repositorios son **interfaces** que extienden `JpaRepository` de Spring Data JPA. Métodos como `findByNumeroDocumento` o `findByEspecialidadAndActivoTrue` no siguen el esquema del Template Method, pues en ese patrón el equipo de desarrollo debe:

1. Escribir una clase abstracta propia con el algoritmo base definido.
2. Escribir subclases concretas que sobreescriban los pasos específicos.

Nada de esto ocurre en el código. Spring Data JPA recibe la declaración del método, interpreta su nombre en tiempo de ejecución y genera automáticamente la implementación SQL sin que el equipo intervenga en ese proceso. El patrón que realmente opera aquí
es el **Repository Pattern**, el cual es un patrón de diseño que actua como intermediario entre el dominio y las capas de mapeo, creando una capa abstracta que desacopla la logica del negocio del acceso a datos, lo cual hace que el codigo sea mas facil de mantener, testear y adaptar a diferentes fuentes de datos. 

Mantener la documentación incorrecta representa un riesgo real ya que si un nuevo integrante del equipo buscara clases abstractas y subclases que no existen, perdiendo tiempo y llegando a conclusiones equivocadas sobre la arquitectura del sistema.

---

## Decisión

Se decide que es necesario corregir la documentación oficial del proyecto para reemplazar la referencia al
patrón Template Method por una descripción precisa del **Repository Pattern** tal
como lo implementa Spring Data JPA en el contexto del dominio médico de Piedra Azul.

La documentación actualizada debe reflejar que:

- Los repositorios del proyecto son interfaces que extienden `JpaRepository<Entidad, ID>`.
- Las consultas específicas del dominio se declaran como métodos con nombres
  semánticos (`findByMedicoAndFecha`, `findHorasOcupadas...`) y Spring Data JPA
- **No existe** herencia ni clase abstracta escrita por el equipo para este propósito.
- El patrón Template Method **no está implementado** en la capa de persistencia
  del proyecto en su estado actual.

---

## Consecuencias

### Positivas

- **Trazabilidad restaurada:** la documentación refleja fielmente lo que el código hace, eliminando la confusión entre un patrón que se creyó implementar y el que realmente opera.
- Un nuevo desarrollador que lea el documento encontrará una descripción coherente con el código fuente desde el primer día.
- Si en algún sprint posterior el equipo decide introducir el Template Method para, por ejemplo, unificar el flujo de generación de reportes, partirá de una línea base documental correcta y no de una que ya contenía un error.

### Negativas / Riesgos

- Esta corrección evidencia que el proceso de documentación estuvo mal hecho y que hay que corregir la documentacion para mantenerla actualizada
- Este ADR corrige únicamente la referencia al patrón en la capa de persistencia. Si el mismo error se repite en otras secciones del documento (diagramas C4, diagramas de secuencia), esas secciones deben ser corregidas por separado.