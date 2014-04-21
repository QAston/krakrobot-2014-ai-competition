#include "wiezamodel.h"

WiezaModel::WiezaModel(QObject *parent) :
    QObject(parent)
{
}

WiezaModel::WiezaModel( QString c, qint32 x, qint32 y)
{
    _color = c;
    _posX = x;
    _posY = y;
}
