package seedu.duke.parsermanager;

import seedu.duke.PropertyList;
import seedu.duke.command.Command;
import seedu.duke.command.CommandDeleteProperty;
import seedu.duke.exception.EmptyDetailException;
import seedu.duke.exception.IncorrectFlagOrderException;
import seedu.duke.exception.InvalidIndexException;
import seedu.duke.exception.MissingFlagException;
import seedu.duke.exception.NotIntegerException;

import java.util.ArrayList;

import static seedu.duke.CommandStructure.DELETE_PROPERTY_FLAGS;
import static seedu.duke.Messages.EXCEPTION;
import static seedu.duke.Messages.MESSAGE_DELETE_PROPERTY_WRONG_FORMAT;
import static seedu.duke.Messages.MESSAGE_INVALID_INDEX;
import static seedu.duke.Messages.MESSAGE_NOT_INTEGER;

public class ParseDeleteProperty extends Parser {
    private final String commandDescription;
    private final PropertyList propertyList;

    public ParseDeleteProperty(String deleteCommandDescription, PropertyList propertyList) {
        this.commandDescription = deleteCommandDescription;
        this.propertyList = propertyList;
    }

    @Override
    public Command parseCommand() throws InvalidIndexException, MissingFlagException, IncorrectFlagOrderException,
            NotIntegerException, EmptyDetailException {
        try {
            checkForEmptyDetails(commandDescription);
            ArrayList<String> deletePropertyDetailsString = processCommandDetails(commandDescription);
            ArrayList<Integer> deletePropertyDetailsInt = convertProcessedCommandDetailsToInteger(
                    deletePropertyDetailsString);

            int propertyIndex = deletePropertyDetailsInt.get(0);
            checkForInvalidPropertyIndexDelete(propertyIndex);
            return new CommandDeleteProperty(propertyIndex);
        } catch (InvalidIndexException e) {
            throw new InvalidIndexException(MESSAGE_INVALID_INDEX);
        } catch (MissingFlagException e) {
            throw new MissingFlagException(MESSAGE_DELETE_PROPERTY_WRONG_FORMAT);
        } catch (IncorrectFlagOrderException e) {
            throw new IncorrectFlagOrderException(MESSAGE_DELETE_PROPERTY_WRONG_FORMAT);
        } catch (NotIntegerException e) {
            throw new NotIntegerException(MESSAGE_NOT_INTEGER);
        } catch (EmptyDetailException e) {
            throw new EmptyDetailException(MESSAGE_DELETE_PROPERTY_WRONG_FORMAT);
        }
    }

    private void checkForEmptyDetails(String commandDetail) throws EmptyDetailException {
        boolean isEmptyDetail = isStringEmpty(commandDetail);
        if (isEmptyDetail) {
            throw new EmptyDetailException(EXCEPTION);
        }
    }

    private ArrayList<String> processCommandDetails(String rawCommandDetail)
            throws MissingFlagException, IncorrectFlagOrderException {

        String[] flags = DELETE_PROPERTY_FLAGS;
        int[] flagIndexPositions = getFlagIndexPositions(rawCommandDetail, flags);
        checkForMissingFlags(flagIndexPositions);
        checkFlagsOrder(flagIndexPositions);
        return extractCommandDetails(rawCommandDetail, flags, flagIndexPositions);
    }

    private ArrayList<Integer> convertProcessedCommandDetailsToInteger(ArrayList<String> processedCommandDetails)
            throws NotIntegerException {
        ArrayList<Integer> integerDetails = new ArrayList<>();
        for (String detail : processedCommandDetails) {
            int integer;
            try {
                integer = Integer.parseInt(detail);
            } catch (NumberFormatException e) {
                throw new NotIntegerException(EXCEPTION);
            }
            integerDetails.add(integer - 1); // Convert to 0-index
        }
        return integerDetails;
    }

    private void checkForInvalidPropertyIndexDelete(int propertyIndex) throws InvalidIndexException {
        int currentListSize = propertyList.getCurrentListSize();
        if (propertyIndex < 0 || propertyIndex >= currentListSize) {
            throw new InvalidIndexException(EXCEPTION);
        }
    }

    private boolean isStringEmpty(String commandDetail) {
        return commandDetail.trim().isEmpty();
    }

    private int[] getFlagIndexPositions(String commandDetails, String[] flags) {
        int[] flagIndexPositions = new int[flags.length];

        for (int i = 0; i < flags.length; i++) {
            flagIndexPositions[i] = commandDetails.indexOf(flags[i]);
        }
        return flagIndexPositions;
    }

    private void checkForMissingFlags(int[] flagIndexPositions) throws MissingFlagException {
        for (int flagIndex : flagIndexPositions) {
            if (!isFlagPresent(flagIndex)) {
                throw  new MissingFlagException(EXCEPTION);
            }
        }
    }

    private void checkFlagsOrder(int[] flagIndexPositions) throws IncorrectFlagOrderException {
        for (int i = 0; i < flagIndexPositions.length - 1; i++) {
            checkForCorrectFlagOrder(flagIndexPositions[i], flagIndexPositions[i + 1]);
        }
    }

    private ArrayList<String> extractCommandDetails(String rawCommandDetail, String[] flags,
                                                    int[] flagIndexPositions) {
        ArrayList<String> extractedCommandDetails = new ArrayList<>();
        for (int i = 0; i < flags.length; i++) {
            String extractedDetail;
            if (i == flags.length - 1) {
                /* The extracted detail for the last flag starts from the char after the flag, to the end of
                   rawCommandDetails */
                extractedDetail = extractDetail(rawCommandDetail, flagIndexPositions[i] + flags[i].length());
            } else {
                // The extracted detail for non-last starts from the char after the flag, to index before the next flag
                extractedDetail = extractDetail(
                        rawCommandDetail,
                        flagIndexPositions[i] + flags[i].length(),
                        flagIndexPositions[i + 1]);
            }
            extractedCommandDetails.add(extractedDetail.trim());
        }
        return extractedCommandDetails;
    }

    private boolean isFlagPresent(int flagIndexPosition) {
        return (flagIndexPosition != -1);
    }

    private void checkForCorrectFlagOrder(int flagPosition, int nextFlagPosition) throws IncorrectFlagOrderException {
        boolean hasCorrectOrder = (flagPosition < nextFlagPosition);
        if (!hasCorrectOrder) {
            throw new IncorrectFlagOrderException(EXCEPTION);
        }
    }

    private static String extractDetail(String rawDetail, int beginIndex) {
        return rawDetail.substring(beginIndex).trim();
    }

    private static String extractDetail(String rawDetail, int beginIndex, int endIndex) {
        return rawDetail.substring(beginIndex, endIndex).trim();
    }
}
