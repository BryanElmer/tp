package seedu.commands;

import seedu.data.ingredient.Ingredient;
import seedu.parser.Parser;
import seedu.storage.IngredientList;
import seedu.ui.Ui;

import java.util.ArrayList;

public class ListCommand extends Command {
    /**
     * Constructor for ListCommand.
     */
    public ListCommand() {
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute(Ui ui, Parser parser, IngredientList ingredientList) {
        String sortType = parser.parseSortTypeForList(ui.getCurrentUserInput());
        ArrayList<Ingredient> listOfIngredients = ingredientList.getIngredientList(sortType);
        ui.printListMessage(listOfIngredients, sortType);
    }
}