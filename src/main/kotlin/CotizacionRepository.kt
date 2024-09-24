package org.example

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.notExists

class CotizacionRepository(){


    fun csvToMap(rutaCsv: Path): Map<String, List<String>>{

        val mapToReturn = mutableMapOf<String, List<String>>()
        val br = Files.newBufferedReader(rutaCsv)

        br.use {flujo ->
            flujo.forEachLine { line ->
                val separatedLine = line.split(";")
                if (line.isNotBlank() && separatedLine[0] != "Nombre"){
                    mapToReturn[separatedLine[0]] = listOf<String>(separatedLine[1],separatedLine[2],separatedLine[3],separatedLine[4],separatedLine[5])
                }
            }
        }

        return mapToReturn
    }

    /**
     * Funcion para crear carpetas de la fecha cuando se ha usado la funcion
     * @return el archivo en el que vamos a escribir el minimo, maximo y media
     */
    private fun createFich(): File{

        val formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatoHora = DateTimeFormatter.ofPattern("HH-mm-ss")
        val Hora = LocalDateTime.now().format(formatoHora)
        val Fecha = LocalDateTime.now().format(formatoFecha)
        val fechaArchivo = LocalDateTime.now().format(formatoFecha)
        val file = File("src/main/resources/MinMaxOfColumns/$fechaArchivo/MinMax_${Fecha}_${Hora}.txt")

        val direcDir = File("src/main/resources/MinMaxOfColumns")
        val dateDir = File("src/main/resources/MinMaxOfColumns/$fechaArchivo")

        if (!direcDir.exists()){
            direcDir.mkdir()
        }
        if (!dateDir.exists()){
            dateDir.mkdir()
        }

        file.createNewFile()

        return file

    }


    fun createFichFromMap(mapOfThings:Map<String, List<String>>){
        val fich = createFich()
        val bw = Files.newBufferedWriter(fich.toPath(),StandardOpenOption.APPEND)


        val listOfNames = listOf<String>("Final","Maximo","Minimo","Volumen","Efectivo")

        val listOfColumns = mutableListOf<Float>()
        for (i in 0..4){


            mapOfThings.values.forEach{listOfNums ->
                val replaced = listOfNums[i].replace(".","").replace(",",".")
                listOfColumns.add(replaced.toFloat())
            }


            bw.use {
                bw.append("${listOfNames[i]}\n")
                bw.append("Min: ${listOfColumns.min()}\n")
                bw.append("Max: ${listOfColumns.max()}\n")
                bw.append("Average: ${listOfColumns.average()}\n")
                bw.append("\n")
                bw.flush()
            }


/*
            bw.append("${listOfNames[i]}\n")
            bw.append("Min: ${listOfColumns.min()}\n")
            bw.append("Max: ${listOfColumns.max()}\n")
            bw.append("Average: ${listOfColumns.average()}\n")
            bw.append("\n")
*/


            listOfColumns.clear()


        }

    }
}