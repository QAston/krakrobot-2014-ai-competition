import QtQuick 2.2
import QtQuick.Controls 1.1

ApplicationWindow {
    visible: true
    width: 640
    height: 480
    title: qsTr("Hello World")

    Canvas {
        id: canvas
        width: 300; height: 300
        anchors.centerIn: parent

        onPaint: {
            // get the drawing context
            var ctx = canvas.getContext('2d')

            // create a rectangle path
            ctx.rect(0, 0, canvas.width, canvas.height);

            // setup fill color
            ctx.fillStyle = "#FFFFFF"

            // fill path
            ctx.fill()

            // setup line width and stroke color
            ctx.lineWidth = 4
            ctx.strokeStyle = "#000000"

            // stroke path
            ctx.stroke()

            var sizeX = canvas.width / 5;
            var sizeY = canvas.height / 5;

             // wyswietl plansze

            for(var i = 0; i < 5; ++i)
            {
                for(var j = 0; j < 5; ++j)
                {
                    ctx.beginPath();
                    ctx.rect(i*sizeX, j*sizeY, sizeX, sizeY);
                    ctx.stroke();
                }
            }

            // wyswietl wieze
            var sizeWieza = 10;

            for(var w in wieze)
            {
                ctx.beginPath();
                var centerX = wieze[w].posX * sizeX + sizeX/2;
                var centerY = wieze[w].posY * sizeY + sizeY/2;
                console.log(centerX - sizeWieza/2, centerY - sizeWieza/2, sizeWieza, sizeWieza);
                ctx.rect(centerX - sizeWieza/2, centerY - sizeWieza/2, sizeWieza, sizeWieza);
                if (wieze[w].col == "blue")
                    ctx.fillStyle = "#0000FF";
                else if (wieze[w].col == "white")
                    ctx.fillStyle = "#000000";
                else
                    ctx.fillStyle = "#FF0000";
                ctx.fill();
            }


            // wyswietl robota
        }
    }

    Button {
        id: button1
        x: 8
        y: 7
        text: qsTr("Button")
    }

    Button {
        id: button2
        x: 107
        y: 7
        text: qsTr("Button")
    }

    menuBar: MenuBar {
        Menu {
            title: qsTr("File")
            MenuItem {
                text: qsTr("Exit")
                onTriggered: Qt.quit();
            }
        }
    }
}
