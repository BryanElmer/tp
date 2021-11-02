package seedu.ui;

import seedu.data.exception.FridgetException;
import seedu.data.item.Item;

import java.util.ArrayList;
import java.util.Scanner;

public class Ui {
    private final Scanner input;
    private static final String SEPARATOR_LINE = "__________________________________________";
    private static final String FOUR_SPACE_INDENTATION = "    ";
    private static final String USER_INPUT = "USER INPUT: ";

    private String currentUserInput;

    public enum CommandType {
        REMOVE,
        UPDATE,
        RESET,
        SHOPRESET
    }

    /**
     * A constructor to initialise ui.
     */
    public Ui() {
        this.input = new Scanner(System.in);
    }

    /**
     * Prints introductory message when Fridget runs.
     */
    public void printIntroduction() {
        String logo = "\n\n\n\n"
                + "   ad88              88           88\n"
                + "  d8\"                \"\"           88                             ,d\n"
                + "  88                              88                             88\n"
                + "MM88MMM  8b,dPPYba,  88   ,adPPYb,88   ,adPPYb,d8   ,adPPYba,  MM88MMM\n"
                + "  88     88P'   \"Y8  88  a8\"    `Y88  a8\"    `Y88  a8P_____88    88\n"
                + "  88     88          88  8b       88  8b       88  8PP\"\"\"\"\"\"\"    88\n"
                + "  88     88          88  \"8a,   ,d88  \"8a,   ,d88  \"8b,   ,aa    88,\n"
                + "  88     88          88   `\"8bbdP\"Y8   `\"YbbdP\"Y8   `\"Ybbd8\"'    \"Y888\n"
                + "                                       aa,    ,88\n"
                + "                                        \"Y8bbdP\"\n";
        String greeting = "Hello!\n"
                + "What would you like to do?";
        String introMessage = logo
                + greeting;
        printLine(introMessage);
        printSeparatorLine();
    }

    /**
     * Prints a line to separate between input and output.
     */
    public void printSeparatorLine() {
        System.out.println(SEPARATOR_LINE);
    }

    /**
     * Print out the content that is reads.
     *
     * @param content String to be printed.
     */
    public void printLine(String content) {
        System.out.println(content);
    }

    /**
     * Prints a reaction to user after successfully adding an item.
     *
     * @param item The item the user has added.
     */
    public void printReactionToAddingItem(Item item) {
        String acknowledgeAdd = "You have successfully added:\n";
        String addReaction = acknowledgeAdd
                + FOUR_SPACE_INDENTATION + item;
        printLine(addReaction);
    }

    /**
     * Prints a reaction to user after successfully adding an item that has existed in the list.
     *
     * @param item The item the user had added.
     */
    public void printReactionToAddingExistingItem(Item item, int qty) {
        String acknowledgeAdd = "You have successfully increased the quantity of:\n";
        String itemString = item.addExistingToString(qty);
        String addReaction = acknowledgeAdd + FOUR_SPACE_INDENTATION + itemString;
        printLine(addReaction);
    }

    /**
     * Prints custom error message when DateTimeParseException detected.
     */
    public void printDateTimeFormat() {
        String errorInfo = "Wrong Date entry format used. Please try the format for date\n\n";
        String correctFormat = "/<YYYY-MM-DD>\n";
        String exampleInput = "Example: \"... /2022-08-03\"";
        String dateErrorMessage = errorInfo
                + FOUR_SPACE_INDENTATION + correctFormat
                + FOUR_SPACE_INDENTATION + exampleInput;
        printLine(dateErrorMessage);
    }

    /**
     * Prints a reaction to user successfully removing an item.
     *
     * @param item The item the user has added.
     */
    public void printReactionToRemovingItem(Item item, int qty) {
        String acknowledgeRemove = "You have successfully removed:\n";
        String removeReaction = acknowledgeRemove
                + FOUR_SPACE_INDENTATION + item.getItemName() + " | Qty: " + qty
                + " | " + item.getColoredExpiryDate();
        printLine(removeReaction);
    }

    /**
     * Suggests the correct item name if input is incomplete.
     *
     * @param predictedItem Only item available containing search term but is not exactly the same.
     */
    public boolean giveSuggestion(Item predictedItem) throws FridgetException {
        String message = String.format("Did you mean: %s? [Y/N]", predictedItem.getItemName());
        printLine(message);
        printSeparatorLine();
        return getYesNo();
    }

    /**
     * Gets user to input "y" or "n".
     *
     * @return true if "y", and false if "n".
     */
    private boolean getYesNo() throws FridgetException {
        printUserInputMessage();
        String answer = readUserInput().toLowerCase().trim();
        printSeparatorLine();
        switch (answer) {
        case "y":
            return true;
        case "n":
            printLine("Understood, Command has been shutdown.");
            return false;
        default:
            throw new FridgetException("Invalid Confirmation. The Command has been shutdown.");
        }
    }

    /**
     * Prints Question to ask user which item is the target item.
     *
     * @param matchingItems The list of items which match the user's search term.
     * @param commandType   Type of command printing the message.
     */
    public void printConfirmItemMessage(ArrayList<Item> matchingItems, CommandType commandType) {
        String question = "Which item would you like to %s? Type the index of the item below.";
        String correctText;
        switch (commandType) {
        case REMOVE:
            correctText = "remove";
            break;
        case UPDATE:
            correctText = "overwrite quantity";
            break;
        default:
            correctText = "";
        }
        printLine(String.format(question, correctText));
        printListOfItems(matchingItems, true);
        printLine("If you've changed your mind, simply type 'quit'.");
        printSeparatorLine();
    }

    /**
     * Prints list of matching items to prompt user to pick the correct match.
     *
     * @param matchingItems List of items that matches the search term.
     * @param commandType   Whether it is UPDATE or REMOVE.
     * @return The item selected by the user.
     * @throws FridgetException If input is out of bounds.
     */
    public Item matchItem(ArrayList<Item> matchingItems, CommandType commandType) throws FridgetException {
        if (matchingItems.size() == 1) {
            return matchingItems.get(0);
        }

        printConfirmItemMessage(matchingItems, commandType);
        int userIntInput = getIntInput(commandType);
        int index = checkAndGetIndex(matchingItems, userIntInput);

        return matchingItems.get(index - 1);
    }

    /**
     * Verifies if index is within bounds.
     *
     * @param matchingItems List of items that are matching.
     * @param intInput      The integer of userInput.
     * @return The input integer if input is within bounds.
     * @throws FridgetException if input Integer is out of Bounds.
     */
    private int checkAndGetIndex(ArrayList<Item> matchingItems, int intInput) throws FridgetException {
        if (intInput <= 0 | intInput > matchingItems.size()) {
            throw new FridgetException("This index is not valid. The command has been shutdown.");
        }
        return intInput;
    }

    /**
     * Get input from user.
     *
     * @return A String containing user input.
     */
    public String readUserInput() {
        currentUserInput = input.nextLine();
        return currentUserInput;
    }

    /**
     * Gets stored user input.
     *
     * @return Stored user input.
     */
    public String getCurrentUserInput() {
        return currentUserInput;
    }

    /**
     * Prints the exit message.
     */
    public void printExitMessage() {
        String reassureUser = "We'll help you remember everything you told us :)\n"
                + "See you again!~~";
        printLine(reassureUser);
    }

    /**
     * Prints a list of items, indented by four spaces and preceded by an index.
     *
     * @param listOfItems The list of items to be printed.
     * @param hasIndex    Boolean value to indicate printing with index.
     */
    public void printListOfItems(ArrayList<Item> listOfItems, boolean hasIndex) {
        int index = 1;
        for (Item item : listOfItems) {
            String beforeItem = FOUR_SPACE_INDENTATION + (hasIndex ? index + ". " : "");
            printLine(beforeItem + item);
            index++;
        }
    }

    /**
     * Prints a shop list of items, indented by four spaces and preceded by an index.
     *
     * @param listOfItems The shop list of items to be printed.
     * @param hasIndex    Boolean value to indicate printing with index.
     */
    public void printShopListOfItems(ArrayList<Item> listOfItems, boolean hasIndex) {
        int index = 1;
        for (Item item : listOfItems) {
            String beforeItem = FOUR_SPACE_INDENTATION + (hasIndex ? index + ". " : "");
            printLine(beforeItem + item.toShopFormat());
            index++;
        }
    }

    /**
     * Prints a message informing user on list being printed.
     *
     * @param listOfItems The list of items of all items in fridget.
     * @param sortType    The string indicating the sort type.
     * @param isShop      Boolean indicating if the message is used to print shopping list.
     */
    public void printListMessage(ArrayList<Item> listOfItems, String sortType, boolean isShop) {
        String listMessage = sortTypeMessage(sortType);
        printLine(listMessage);
        if (!isShop) {
            printListOfItems(listOfItems, false);
        } else {
            printShopListOfItems(listOfItems, true);
        }
    }

    /**
     * Determine sortType message.
     *
     * @param sortType "e", "r", or "default".
     * @return sortTypeMessage.
     */
    public String sortTypeMessage(String sortType) {
        switch (sortType) {
        case "e":
            return ("List sorted by expiry date:");
        case "r":
            return ("List sorted by earliest added:");
        default:
            return ("List sorted by item name:");
        }
    }

    /**
     * Prints a list of matching item for the find command.
     *
     * @param listOfItems The list of matching items to print.
     */
    public void printListOfMatchingItems(ArrayList<Item> listOfItems) {
        if (listOfItems.isEmpty()) {
            String noMatchingItem = "No matching item found!";
            printLine(noMatchingItem);
        } else {
            String resultsHeader = "These are the matching items:";
            printLine(resultsHeader);
            printListOfItems(listOfItems, true);
        }
    }

    /**
     * Prints only list of items/item away from group.
     *
     * @param listOfItems list of Items nearing expiry only.
     */
    public void printExpiringMessage(ArrayList<Item> listOfItems) {
        ArrayList<Item> expiringList = new ArrayList<>();
        for (Item item : listOfItems) {
            if (item.isNearExpiry()) {
                expiringList.add(item);
            }
        }

        if (expiringList.size() == 0) {
            printLine("No items expiring.");
        } else {
            String expiringMessage = "Expiring/Expired Items:";
            printLine(expiringMessage);
            printListOfItems(expiringList, true);
        }
    }

    /**
     * Prints message if there are no matching items.
     *
     * @param matchingItems List of matching items after parseSearchTerm.
     */
    public void printIfNotFoundMessage(ArrayList<Item> matchingItems) {
        if (matchingItems.size() == 0) {
            printLine("No such item exists.");
        }
    }

    /**
     * Asks user for input and expect only Integer input.
     *
     * @return The integer of the input.
     * @throws FridgetException if userInput is not integer.
     */
    public int getIntInput() throws FridgetException {
        printUserInputMessage();
        String toIntInput = readUserInput();
        printSeparatorLine();

        int inputInt = checkInt(toIntInput);
        return inputInt;
    }

    /**
     * Asks user for input and expect only Integer input.
     *
     * @return The integer of the input.
     * @throws FridgetException if userInput is not integer.
     */
    public int getIntInput(CommandType commandType) throws FridgetException {
        printUserInputMessage();
        String toIntInput = readUserInput();
        printSeparatorLine();

        if (toIntInput.toLowerCase().matches("quit")) {
            if (commandType.equals(CommandType.REMOVE)) {
                throw new FridgetException("You have decided to quit. The remove command has been shutdown.");
            } else if (commandType.equals(CommandType.UPDATE)) {
                throw new FridgetException("You have decided to quit. The update command has been shutdown.");
            }
        }

        int inputInt = checkInt(toIntInput);
        return inputInt;
    }

    /**
     * Ensures integer is within upper bound and 0.
     *
     * @param toInt String to be changed to.
     * @return Integer from input String after being checked.
     * @throws FridgetException If String is not integer or integer is invalid.
     */
    private int checkInt(String toInt) throws FridgetException {
        try {
            long toIntOutput = Long.parseLong(toInt);
            if (toIntOutput < 0) {
                throw new FridgetException("Input number cannot be less than 0.");
            } else if (toIntOutput > Integer.MAX_VALUE) {
                throw new FridgetException("Input number cannot be more than " + Integer.MAX_VALUE);
            }
            return (int) toIntOutput;
        } catch (NumberFormatException e) {
            throw new FridgetException("No valid number was stated. The command has been shutdown.");
        }
    }

    /**
     * Get the integer to update item quantity to.
     *
     * @param targetItem Item to update quantity.
     * @return Int input from user.
     * @throws FridgetException if input is invalid.
     */
    public int getUpdate(Item targetItem) throws FridgetException {
        String message = String.format("How many of %s do you have left?", targetItem.getItemName());
        printLine(message);
        printSeparatorLine();
        return getIntInput();
    }

    /**
     * Suggests removing item if update amoount is zero. TODO: Impleement in next iteration.
     *
     * @param targetItem item being updated.
     * @return if user accepts the suggestion.
     * @throws FridgetException if user inputs invalid input.
     */
    public boolean suggestRemove(Item targetItem) throws FridgetException {
        String suggestion = String.format("You have input \"0\". This will remove all %d %s from your list. \n"
                        + "Do you still wish to proceed? [Y/N]",
                targetItem.getQuantity(), targetItem.getItemName());
        printLine(suggestion);
        return getYesNo();
    }

    /**
     * Prints message to inform on successful change.
     *
     * @param updated Lastest update on item.
     */
    public void acknowledgeUpdate(Item updated) {
        String msg = String.format("Quantity of %s is now %d.", updated.getItemName(), updated.getQuantity());
        printLine(msg);
    }

    /**
     * Gets the quantity of items to be removed from the user.
     *
     * @param item Item to be removed.
     * @return Amount of items to be removed.
     * @throws FridgetException If the user types a wrong value (non-integer or 0 or outside limit of quantity)
     */
    public int getQuantityToBeRemoved(Item item) throws FridgetException {
        int limit = item.getQuantity();
        if (limit == 1) {
            return 1;
        }

        printLine("There are " + limit + " items, how many would like to remove?");
        printSeparatorLine();
        int qty = getIntInput();

        if (qty == 0) {
            throw new FridgetException("No items have been removed.");
        }
        if (qty < 0 | qty > limit) {
            throw new FridgetException("This quantity is not valid. The remove command has been shutdown.");
        }
        return qty;
    }

    /**
     * Prints a confirmation message to add a removed item into the shopping list and returns the quantity to be
     * added into the shopping list.
     *
     * @param itemRemoved The item removed.
     * @param qtyInShop   The quantity of removed item already in the shoppingList.
     * @return Quantity of item to be added into the shopping list.
     * @throws FridgetException If the user types a wrong value (non-integer or 0 or outside limit of quantity)
     */
    public int getShopQuantity(Item itemRemoved, int qtyInShop) throws FridgetException {
        String addConfirmMessage = "You have ran out of " + itemRemoved.getItemName()
                + ". Would you like to add it to your shopping list? (Y/N)";
        printSeparatorLine();
        printLine(addConfirmMessage);
        printSeparatorLine();

        if (getYesNo()) {
            String askQuantityMessage = "How many items would you like to buy?";
            if (qtyInShop > 0) {
                askQuantityMessage = "You have " + qtyInShop + " " + itemRemoved.getItemName()
                        + " in the shopping list. " + askQuantityMessage;
            }
            printLine(askQuantityMessage);
            printSeparatorLine();
            int qty = getIntInput();

            if (qty == 0) {
                throw new FridgetException("No items have been added.");
            }
            if (qty < 0) {
                throw new FridgetException("This quantity is not valid. Shutting down the command...");
            }
            return qty;
        }
        return 0;
    }

    /**
     * Prints the reaction after adding item into the shopping list.
     *
     * @param addedItem          Item added into the shopping list.
     * @param qtyInShopBeforeAdd Quantity of item that already existed in the shoppingList.
     */
    public void printShopUpdateMessage(Item addedItem, int qtyInShopBeforeAdd) {
        String addReaction;
        String acknowledgeAdd = "You have successfully added:\n";
        if (qtyInShopBeforeAdd == 0) {
            addReaction = acknowledgeAdd + FOUR_SPACE_INDENTATION + addedItem.toShopFormat();
        } else {
            addReaction = acknowledgeAdd + FOUR_SPACE_INDENTATION
                    + addedItem.toAddExistingShopFormat(qtyInShopBeforeAdd);
        }
        printLine(addReaction);
    }

    /**
     * Prints a reconfirmation message and gets the reconfirmation result.
     *
     * @return Boolean representing reconfirm status (y: confirm, n: abort)
     */
    public boolean getResetReconfirm(CommandType commandType) {
        printLine(getResetQuestion(commandType));
        printSeparatorLine();
        printUserInputMessage();
        String confirm = readUserInput();
        if (!confirm.equalsIgnoreCase("y")) {
            printSeparatorLine();
            printLine("Shutting down the command...");
        }
        return confirm.equalsIgnoreCase("y");
    }

    /**
     * Returns the reset question according to command.
     *
     * @param commandType The command used.
     * @return String containing the reset question.
     */
    public String getResetQuestion(CommandType commandType) {
        switch (commandType) {
        case RESET:
            return "Are you sure you want to reset everything in the fridge? (Y/N)";
        case SHOPRESET:
            return "Are you sure you want to reset everything in the shopping list? (Y/N)";
        default:
            return "";
        }
    }


    /**
     * Prints the item list reset message.
     */
    public void printResetMessage() {
        printSeparatorLine();
        printLine("Item list has been reset successfully.");
    }

    /**
     * Prints a prompt for user input.
     */
    public void printUserInputMessage() {
        System.out.print(USER_INPUT);
    }

    /**
     * Prints the shopping list reset message.
     */
    public void printShopResetMessage() {
        printSeparatorLine();
        printLine("Shopping list has been reset successfully.");
    }
}
