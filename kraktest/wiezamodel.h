#ifndef WIEZAMODEL_H
#define WIEZAMODEL_H

#include <QObject>

class WiezaModel : public QObject
{
    Q_OBJECT
public:
    explicit WiezaModel(QObject *parent = 0);
    explicit WiezaModel(QString c, qint32 x, qint32 y);

    Q_PROPERTY(QString col READ col)
    Q_PROPERTY(qint32 posX READ posX)
    Q_PROPERTY(qint32 posY READ posY)

    virtual QString col() const
    {
        return _color;
    }

    virtual qint32 posX() const
    {
        return _posX;
    }

    virtual qint32 posY() const
    {
        return _posY;
    }

signals:

public slots:

private:
    QString _color;
    qint32 _posX;
    qint32 _posY;

};

#endif // WIEZAMODEL_H
