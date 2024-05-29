package server.commands;

import server.managers.CollectionManager;

public class CountLessThanAnnualTurnover extends ServerCommand {

    public CountLessThanAnnualTurnover(Integer annualTurnover) {
        super(null, annualTurnover);
    }

    @Override
    public String execute(CollectionManager collectionManager) {
        Integer annualTurnover = (Integer) getArgument();
        int count = collectionManager.countLessThanAnnualTurnover(annualTurnover);
        return "Количество элементов со значением annualTurnover меньше " +  annualTurnover + ": " + count;
    }
}
