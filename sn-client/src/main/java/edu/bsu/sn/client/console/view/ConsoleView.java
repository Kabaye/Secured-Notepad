package edu.bsu.sn.client.console.view;

import edu.bsu.sn.client.notepad.model.FileContent;
import edu.bsu.sn.client.notepad.model.UserFiles;
import org.springframework.stereotype.Component;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Dimension;
import java.util.Objects;

/**
 * @author svku0919
 * @version 27.09.2020
 */
@Component
public class ConsoleView {
    public static final String LOGIN = "login";
    public static final String GET_FILE = "get-file";
    public static final String GET_FILES = "get-files";
    public static final String DELETE_FILE = "delete-file";
    public static final String UPDATE_FILE = "update-file";
    public static final String ADD_FILE = "add-file";
    public static final String LOGOUT = "logout";
    public static final String HELP = "help";
    public static final String EXIT = "exit";

    private final JFrame jFrame;

    public ConsoleView() {
        jFrame = new JFrame();
        jFrame.setVisible(false);
        jFrame.setAlwaysOnTop(true);
    }

    public void printHelp() {
        System.out.println("""
                Your commands: 
                            login          --- login into system;
                            get-file       --- get file content;
                            get-files      --- get all user files;
                            delete-file    --- delete user file;
                            update-file    --- update user file;
                            add-file       --- add user file;
                            logout         --- logout from system;
                            help           --- print commands list
                            exit           --- close program;
                """);
    }

    public String askForUsername() {
        return Objects.requireNonNull(JOptionPane
                .showInputDialog(jFrame, "<html><h2>Please, write username:"));
    }

    public String askForFileName() {
        return Objects.requireNonNull(JOptionPane
                .showInputDialog(jFrame, "<html><h2>Please, write file name:"));
    }

    public void printError(Exception ignored) {
        System.out.println("Exception are caught with message: " + ignored.getMessage());
    }

    public void viewFile(FileContent fileContent) {
        JTextArea textArea = new JTextArea(fileContent.getFileContent());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setPreferredSize(new Dimension(500, 500));
        JOptionPane.showMessageDialog(jFrame, new JScrollPane(textArea), fileContent.getFileName(),
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void viewFiles(UserFiles userFiles) {
        JOptionPane.showMessageDialog(jFrame, userFiles.getFileNames().toArray(), "User files",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void viewFileForUpdate(FileContent fileContent) {
        JTextArea textArea = new JTextArea(fileContent.getFileContent());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setPreferredSize(new Dimension(500, 500));

        JOptionPane.showMessageDialog(jFrame, new JScrollPane(textArea), fileContent.getFileName(),
                JOptionPane.INFORMATION_MESSAGE);

        fileContent.setFileContent(textArea.getText());
    }
}
