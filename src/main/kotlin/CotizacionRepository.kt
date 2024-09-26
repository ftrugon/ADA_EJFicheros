package org.example

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.notExists
import kotlin.text.replace

class CotizacionRepository(
    val csvPath: Path
){


    fun csvToMap(): Map<String, List<String>>{

        val mapToReturn = mutableMapOf<String, List<String>>()
        val br = Files.newBufferedReader(csvPath)

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
        if (!file.exists()){
            file.createNewFile()
        }
        return file
    }


    fun createFichFromMapByColumn(mapOfThings:Map<String, List<String>>){
        val fich = createFich()

        val br = Files.newBufferedReader(csvPath)
        val listOfNames = br.readLine().split(";").toMutableList()
        listOfNames.removeFirst()
        br.close()

        val addToBw = mutableListOf<String>()

        val listOfColumns = mutableListOf<Float>()
        for (i in 0..listOfNames.count()-1){

            mapOfThings.values.forEach{listOfNums ->
                val replaced = listOfNums[i].replace(".","").replace(",",".")
                listOfColumns.add(replaced.toFloat())
            }

            addToBw.add("${listOfNames[i]}\nMin: ${listOfColumns.min()}\nMax: ${listOfColumns.max()}\nAverage: ${listOfColumns.average()}\n\n")
/*
            fich.appendText("${listOfNames[i]}\n")
            fich.appendText("Min: ${listOfColumns.min()}\n")
            fich.appendText("Max: ${listOfColumns.max()}\n")
            fich.appendText("Average: ${listOfColumns.average()}\n")
            fich.appendText("\n")

            bw.use {
                bw.append("${listOfNames[i]}\n")
                bw.append("Min: ${listOfColumns.min()}\n")
                bw.append("Max: ${listOfColumns.max()}\n")
                bw.append("Average: ${listOfColumns.average()}\n")
                bw.append("\n")
                bw.flush()
            }

            bw.append("${listOfNames[i]}\n")
            bw.append("Min: ${listOfColumns.min()}\n")
            bw.append("Max: ${listOfColumns.max()}\n")
            bw.append("Average: ${listOfColumns.average()}\n")
            bw.append("\n")
*/
            listOfColumns.clear()
        }

        val bw = Files.newBufferedWriter(fich.toPath(),StandardOpenOption.APPEND)
        bw.use {
            addToBw.forEach {
                bw.write(it)
            }
        }

    }

    fun createFichFromMapByRow(mapOfThings:Map<String, List<String>>){
        val fich = createFich()

        val listOfNames = listOf<String>("Nombre","Min","Max","Promedio")

        fich.appendText(listOfNames.toString())

        val addToBw = mutableListOf<String>()

        mapOfThings.forEach{name, numbers ->
            val formatedNumbers = mutableListOf<Float>()
            numbers.forEach{ numString ->
                val replaced = numString.replace(".","").replace(",",".")
                formatedNumbers.add(replaced.toFloat())
            }
            addToBw.add("\n$name, min: ${formatedNumbers.min()},max: ${formatedNumbers.max()},avg: ${formatedNumbers.average()}")
            //fich.appendText("\n$name, min: ${formatedNumbers.min()},max: ${formatedNumbers.max()},avg: ${formatedNumbers.average()}")
            formatedNumbers.clear()
        }


        val bw = Files.newBufferedWriter(fich.toPath(),StandardOpenOption.APPEND)
        bw.use {
            addToBw.forEach {
                bw.write(it)
            }
        }

    }


}