package seedu.duke.parsermanager;

import seedu.duke.command.Command;
import seedu.duke.command.CommandListProperties;
import seedu.duke.exception.EmptyDescriptionException;

import static seedu.duke.Messages.MESSAGE_EMPTY_DESCRIPTION;

public class ParseListProperty extends Parser {

    private final String commandDetail;

    public ParseListProperty(String listPropertyCommandDescription) {
        this.commandDetail = listPropertyCommandDescription;
    }

    @Override
    public Command parseCommand() throws EmptyDescriptionException {
        checkForEmptyDescription(commandDetail);
        return new CommandListProperties();
    }

    private void checkForEmptyDescription(String commandDetail) throws EmptyDescriptionException {
        boolean isEmptyDescription = isEmptyString(commandDetail);
        if (isEmptyDescription) {
            throw new EmptyDescriptionException(MESSAGE_EMPTY_DESCRIPTION);
        }
    }

    private boolean isEmptyString(String commandDetail) {
        return commandDetail.trim().isEmpty();
    }
}
