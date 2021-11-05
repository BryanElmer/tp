package seedu.commands;

import seedu.data.exception.FridgetException;
import seedu.parser.Parser;
import seedu.storage.ItemList;
import seedu.storage.ShoppingList;
import seedu.ui.Ui;

//@@author BryanElmer
public class ResetCommand extends Command {
    /**
     * Constructor for ResetCommand.
     */
    public ResetCommand() {
    }

    /**
     * Executes the "reset" command.
     *
     * @param ui           The ui object to interact with user.
     * @param parser       The parser object to parse user inputs.
     * @param itemList     The itemList object.
     * @param shoppingList The shoppingList object.
     * @throws FridgetException The error object thrown.
     */
    @Override
    public void execute(Ui ui, Parser parser, ItemList itemList, ShoppingList shoppingList)
            throws FridgetException {
        if (itemList.getItemList("r").isEmpty()) {
            throw new FridgetException("You currently have nothing in your fridge.");
        }
        if (ui.getResetReconfirm(Ui.CommandType.RESET)) {
            itemList.resetList();
            ui.printResetMessage();
        }
    }
}
//@@author BryanElmer
