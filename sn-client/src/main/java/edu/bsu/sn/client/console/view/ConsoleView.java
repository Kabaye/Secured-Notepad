package edu.bsu.sn.client.console.view;

import edu.bsu.sn.client.notepad.model.FileContent;
import edu.bsu.sn.client.notepad.model.UserFiles;
import edu.bsu.sn.client.security.model.LogInUser;
import org.springframework.stereotype.Component;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Dimension;
import java.nio.charset.StandardCharsets;
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
                            get-files      --- get all sessionKeyRequest files;
                            delete-file    --- delete sessionKeyRequest file;
                            update-file    --- update sessionKeyRequest file;
                            add-file       --- add sessionKeyRequest file;
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

    public void viewUserData(LogInUser logInUser) {
        JOptionPane.showMessageDialog(jFrame, new String[]{"Username: " + logInUser.getUsername(),
                        "Password: " + new String(logInUser.getPassword(), StandardCharsets.UTF_8)},
                "Your credentials:", JOptionPane.INFORMATION_MESSAGE);
    }

    public byte[] askForPassword() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("<html><h2>Enter a password:");
        JPasswordField pass = new JPasswordField(25);
        panel.add(label);
        panel.add(pass);
        String[] options = new String[]{"OK", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, panel, "Password",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
        if (option == 0) // pressing OK button
        {
            return new String(pass.getPassword()).getBytes();
        }
        throw new RuntimeException("You must enter password!");
    }

    public void printStr(String s) {
        System.out.println(s);
    }
}
