# graphex
Ein Programm das im Informatikunterricht der Oberstufe zur Einführung in das Thema "Finden von kürzesten Wegen in Graphen" eingesetzt werden soll.

## Nutzungsanleitung

# Starten von Graphex (WIP)
Herunterladen der aktuellen Version aus dem "release" Ordner. Entpacken der Zipdateie in einen Zielordner. 
Zur Verwendung wird aktuell Java jdk-11 benötigt (zufinden [OpenJdk](https://jdk.java.net/java-se-ri/11), oder unter [AdoptOpenJdk](https://adoptopenjdk.net/).
Wenn diese installiert sind mit der Kommandozeile in den Ordner wechseln in dem die Dateien entpackt sind und dort den Befehl "java -jar graphex-1.0.jar" eingeben. Alternativ kann unter Windows das Programm auch mit einem Doppelklick auf die .jar gestartet werden.
Danach öffnet sich das Program und kann genutzt werden.
Passwort zur Freischaltung von Tips: Algorithmus

# Speicherort der Vorlagen
Die Vorlagen die im Programm zur Verfügung stehen werden im Ordner "Graphex\resources\graphex2021\GraphData\Templates" gespeichert. 
Wie auch bei Graphen die von extern Orten geladen werden, kann ein Hintergrundbild hinzugefügt werden, wenn es im gleichen Verzeichnis liegt und den gleichen Namen hat wie der Graph.
Unterstützte Bild-Formate sind: jpeg, jpg, png und bmp.

# Format der JSON-Graph Datei
Graphen für GraphEX werden im JSON Format gespeichert. Zusätzlich können Hintergrundbilder hinzugefügt werden, wenn sie im geleichen Verzeichnis wie der Graph liegen und den gleichen Dateinamen haben.
Das JSON muss den "type": "GXGraph" haben.  
Dann können Knoten-Objekte in der Liste "vertices" hinzugefügt werden. Ein Knoten hat die Attribute "name", "posx" und "posy". "posx" und "posy" sind relative Koordinaten zur Fenstergröße und werden im Intervall von 0-1000 angegeben.  
Kanten-Objekte können der Liste "edges" hinzugefügt werden. Eine Kante hat die Attribute "vertex1", "vertex2" und "weight".  
Zusätzlich muss für jeden Graphen ein "startVertex" und ein "endVertex" angegeben werden, der den gleichen Namen wie einer der vorher definierten Knoten haben muss. Diese Attribute müssen für jeden Graphen gesetzt werde, auch wenn der später verwendete Algorithmus diese möglicherweise nicht verwendet.  
Beispiel:
```
{  
    "type": "GXGraph",  
    "vertices": [  
        {  
            "name": "A",  
            "posx": "100",  
            "posy": "200"  
        },  
        {  
            "name": "B",  
            "posx": "300",  
            "posy": "400",  
        },  
        {  
            "name": "C",  
            "posx": "500",  
            "posy": "600",  
        }  
    ],  
    "edges": [  
        {  
            "vertex1": "A",  
            "vertex2": "B",  
            "weight": "3",    
        },  
        {  
            "vertex1": "A",  
            "vertex2": "C",  
            "weight": "5",  
        },  
        {  
            "vertex1": "B",  
            "vertex2": "C",  
            "weight": "33",  
        }  
    ],  
    "startVertex": "A",  
    "endVertex": "C"  
}

```

# Lizenz
Dieses Projekt ist unter der GNU General Public License (GPL) lizensiert.

# Dependencies
[JavaFXSmartGraph](https://github.com/brunomnsilva/JavaFXSmartGraph) (MIT License)
[everit-json](https://github.com/everit-org/json-schema) (Apache 2.0 License, siehe [LICENSE-Apache.txt](https://github.com/paul-rink/graphex/files/6083580/LICENSE-Apache.txt))
[JSON](https://www.json.org/json-en.html) (MIT License)

[JavaFX](https://github.com/openjdk/jfx) (EULA License)
