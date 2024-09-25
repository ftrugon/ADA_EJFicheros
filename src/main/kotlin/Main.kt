package org.example

import java.nio.file.Path

/*
El fichero cotizacion.csv (que podéis encontrar en la carpeta ficheros)
contiene las cotizaciones de las empresas del IBEX35 con las siguientes
columnas:
Nombre (nombre de la empresa),
Final (precio de la acción al cierre de bolsa),
Máximo (precio máximo de la acción durante la jornada),
Mínimo (precio mínimo de la acción durante la jornada),
Volumen (Volumen al cierre de bolsa),
Efectivo (capitalización al cierre en miles de euros).

        Construir una función reciba el fichero de cotizaciones y
        devuelva un diccionario con los datos del fichero por columnas.

        Construir una función que reciba el diccionario devuelto por la
        función anterior y cree un fichero en formato csv con el mínimo, el
        máximo y la media de dada columna.
 */

fun main() {

    val pathOfCsv = Path.of("C:\\Users\\fran\\IdeaProjects\\ADA_EJFicheros\\src\\main\\resources\\cotizacion.csv")

    val CR = CotizacionRepository(pathOfCsv)

    val mapOfEnterprises = CR.csvToMap()

//    CR.createFichFromMapByColumn(mapOfEnterprises)
    CR.createFichFromMapByRow(mapOfEnterprises)
}