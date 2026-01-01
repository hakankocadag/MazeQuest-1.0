//Hakan
package mazegame;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultListModel;

public class CommandProcessor {
    private static final Pattern REPEAT_WITH_ACTION = Pattern.compile(
        "(GO|LEFT|RIGHT)\\((\\d+),(GO|LEFT|RIGHT|CATCH)\\)", Pattern.CASE_INSENSITIVE);
    private static final Pattern SIMPLE_REPEAT = Pattern.compile(
        "(GO|LEFT|RIGHT)\\((\\d+)\\)", Pattern.CASE_INSENSITIVE);
    private static final Pattern SIMPLE_COMMAND = Pattern.compile(
        "(GO|LEFT|RIGHT|CATCH)\\(\\)", Pattern.CASE_INSENSITIVE);

    public String parseInput(String userInputCommand) {
        Matcher commandMatcher = REPEAT_WITH_ACTION.matcher(userInputCommand);
        if (commandMatcher.matches()) {
            return commandMatcher.group(1).toUpperCase() + "(" + commandMatcher.group(2) + "," + commandMatcher.group(3).toUpperCase() + ")";
        }
        
        commandMatcher = SIMPLE_REPEAT.matcher(userInputCommand);
        if (commandMatcher.matches()) {
            return commandMatcher.group(1).toUpperCase() + "(" + commandMatcher.group(2) + ")";
        }
        
        commandMatcher = SIMPLE_COMMAND.matcher(userInputCommand);
        if (commandMatcher.matches()) {
            return commandMatcher.group(1).toUpperCase() + "()";
        }
        
        return null;
    }

    public void reNumberCommands(DefaultListModel<String> commandListModel) {
        ArrayList<String> commandsWithoutLineNumbers = new ArrayList<>();
        for (int commandIndex = 0; commandIndex < commandListModel.size(); commandIndex++) {
            String numberedCommand = commandListModel.getElementAt(commandIndex);
            commandsWithoutLineNumbers.add(numberedCommand.substring(numberedCommand.indexOf(":") + 2));
        }
        commandListModel.clear();
        for (int commandIndex = 0; commandIndex < commandsWithoutLineNumbers.size(); commandIndex++) {
            commandListModel.addElement((commandIndex + 1) + ": " + commandsWithoutLineNumbers.get(commandIndex));
        }
    }

    public ArrayList<Character> getExecutionSequence(DefaultListModel<String> commandListModel) {
        ArrayList<Character> actionSequence = new ArrayList<>();
        for (int commandIndex = 0; commandIndex < commandListModel.size(); commandIndex++) {
            String numberedCommandLine = commandListModel.getElementAt(commandIndex);
            String cleanCommand = numberedCommandLine.substring(numberedCommandLine.indexOf(":") + 2);
            
            Matcher commandMatcher = REPEAT_WITH_ACTION.matcher(cleanCommand);
            if (commandMatcher.matches()) {
                int repetitionCount = Integer.parseInt(commandMatcher.group(2));
                char primaryActionCharacter = commandMatcher.group(1).toUpperCase().charAt(0);
                char followingActionCharacter = commandMatcher.group(3).toUpperCase().charAt(0);
                for (int repetitionIndex = 0; repetitionIndex < repetitionCount; repetitionIndex++) actionSequence.add(primaryActionCharacter);
                actionSequence.add(followingActionCharacter);
                continue;
            }
            
            commandMatcher = SIMPLE_REPEAT.matcher(cleanCommand);
            if (commandMatcher.matches()) {
                int repetitionCount = Integer.parseInt(commandMatcher.group(2));
                char actionCharacter = commandMatcher.group(1).toUpperCase().charAt(0);
                for (int repetitionIndex = 0; repetitionIndex < repetitionCount; repetitionIndex++) actionSequence.add(actionCharacter);
                continue;
            }
            
            if (cleanCommand.startsWith("GO")) actionSequence.add('G');
            else if (cleanCommand.startsWith("LEFT")) actionSequence.add('L');
            else if (cleanCommand.startsWith("RIGHT")) actionSequence.add('R');
            else if (cleanCommand.startsWith("CATCH")) actionSequence.add('C');
        }
        return actionSequence;
    }
    
    public ArrayList<Integer> getLineMapping(DefaultListModel<String> commandListModel) {
        ArrayList<Integer> sourceLineIndexMapping = new ArrayList<>();
        for (int commandIndex = 0; commandIndex < commandListModel.size(); commandIndex++) {
            String numberedCommandLine = commandListModel.getElementAt(commandIndex);
            String cleanCommand = numberedCommandLine.substring(numberedCommandLine.indexOf(":") + 2);
            
            Matcher commandMatcher = REPEAT_WITH_ACTION.matcher(cleanCommand);
            if (commandMatcher.matches()) {
                int repetitionCount = Integer.parseInt(commandMatcher.group(2));
                for (int repetitionIndex = 0; repetitionIndex < repetitionCount + 1; repetitionIndex++) sourceLineIndexMapping.add(commandIndex);
                continue;
            }
            
            commandMatcher = SIMPLE_REPEAT.matcher(cleanCommand);
            if (commandMatcher.matches()) {
                int repetitionCount = Integer.parseInt(commandMatcher.group(2));
                for (int repetitionIndex = 0; repetitionIndex < repetitionCount; repetitionIndex++) sourceLineIndexMapping.add(commandIndex);
                continue;
            }
            
            sourceLineIndexMapping.add(commandIndex);
        }
        return sourceLineIndexMapping;
    }
    
    public ArrayList<Character> getExecutionSequenceFromCommands(ArrayList<String> commandList) {
        ArrayList<Character> actionSequence = new ArrayList<>();
        for (String currentCommand : commandList) {
            Matcher commandMatcher = REPEAT_WITH_ACTION.matcher(currentCommand);
            if (commandMatcher.matches()) {
                int repetitionCount = Integer.parseInt(commandMatcher.group(2));
                char primaryActionCharacter = commandMatcher.group(1).toUpperCase().charAt(0);
                char followingActionCharacter = commandMatcher.group(3).toUpperCase().charAt(0);
                for (int repetitionIndex = 0; repetitionIndex < repetitionCount; repetitionIndex++) actionSequence.add(primaryActionCharacter);
                actionSequence.add(followingActionCharacter);
                continue;
            }
            
            commandMatcher = SIMPLE_REPEAT.matcher(currentCommand);
            if (commandMatcher.matches()) {
                int repetitionCount = Integer.parseInt(commandMatcher.group(2));
                char actionCharacter = commandMatcher.group(1).toUpperCase().charAt(0);
                for (int repetitionIndex = 0; repetitionIndex < repetitionCount; repetitionIndex++) actionSequence.add(actionCharacter);
                continue;
            }
            
            if (currentCommand.startsWith("GO")) actionSequence.add('G');
            else if (currentCommand.startsWith("LEFT")) actionSequence.add('L');
            else if (currentCommand.startsWith("RIGHT")) actionSequence.add('R');
            else if (currentCommand.startsWith("CATCH")) actionSequence.add('C');
        }
        return actionSequence;
    }
    
    public ArrayList<Integer> getLineMappingFromCommands(ArrayList<String> commandList) {
        ArrayList<Integer> sourceLineIndexMapping = new ArrayList<>();
        for (int commandIndex = 0; commandIndex < commandList.size(); commandIndex++) {
            String currentCommand = commandList.get(commandIndex);
            
            Matcher commandMatcher = REPEAT_WITH_ACTION.matcher(currentCommand);
            if (commandMatcher.matches()) {
                int repetitionCount = Integer.parseInt(commandMatcher.group(2));
                for (int repetitionIndex = 0; repetitionIndex < repetitionCount + 1; repetitionIndex++) sourceLineIndexMapping.add(commandIndex);
                continue;
            }
            
            commandMatcher = SIMPLE_REPEAT.matcher(currentCommand);
            if (commandMatcher.matches()) {
                int repetitionCount = Integer.parseInt(commandMatcher.group(2));
                for (int repetitionIndex = 0; repetitionIndex < repetitionCount; repetitionIndex++) sourceLineIndexMapping.add(commandIndex);
                continue;
            }
            
            sourceLineIndexMapping.add(commandIndex);
        }
        return sourceLineIndexMapping;
    }
}