#include <QApplication>
#include <QQmlApplicationEngine>
#include <QQmlContext>
#include <QObject>
#include "wiezamodel.h"


int main(int argc, char *argv[])
{
    QApplication app(argc, argv);

    QList<QObject*> dataList;
    dataList.append(new WiezaModel("blue", 0, 0));
    dataList.append(new WiezaModel("blue", 1, 1));
    dataList.append(new WiezaModel("blue", 2, 2));

    QQmlApplicationEngine engine;
    engine.load(QUrl(QStringLiteral("qrc:///main.qml")));

    QQmlContext *ctxt = engine.rootContext();
    ctxt->setContextProperty("wieze", QVariant::fromValue(dataList));
    return app.exec();
}
