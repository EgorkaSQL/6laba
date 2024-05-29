package client.generatorss;

import client.mycollection.Coordinates;

public class CoordinateGenerator extends AbstractGenerator {
    public Coordinates createCoordinatesFromUserInput() {
        float x = getValidatedFloat("Введите координаты X: ", null, 583f);
        float y = getValidatedFloat("Введите координаты Y: ", -744f, null);

        return new Coordinates(x, y);
    }
}
