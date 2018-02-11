/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author Alexander Dodd
 */
public class Logger {

    //true if each new message should be written to the standard output
    private boolean stdOut = true;

    //the file name to write out each new message to
    private String writeOutFile = "";

    //the last message updated to the Logger
    private String lastMessage = "";

    //the singleton instance of this class
    private static Logger instance = null;

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    private Logger() {

    }

    //writes out the last updated message to the standard output and the
    //log file depending on the boolean attribute settings
    private void doOut(boolean writeOut) {
        if (stdOut) {
            System.out.println(lastMessage);
        }

        if (writeOut) {
            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(writeOutFile, true)));

                out.println(lastMessage);
                out.close();

            } catch (Exception e) {

            } finally {
            }

        }
    }

    /**
     * Set whether to write out updated messages to the standard output
     * @param stdOut true if messages should be written to the stand output, 
     * and false if not
     */
    public void setStdOut(boolean stdOut) {
        this.stdOut = stdOut;
    }

    /**Update the the Log with a new message. Will be written out to the appropriate
     * channels depending on the settings of the module
     * 
     * @param newMessage the message to log
     * @param writeOut true if the message should be written to the log file, and 
     * false if not
     */
    public void updateLastMessage(String newMessage, boolean writeOut) {
        lastMessage = newMessage;
        doOut(writeOut);
    }

    /**Sets the filename of the log file
     * 
     * @param str the filename of the log file
     */
    public void setWriteOutFileName(String str) {
        writeOutFile = str;
    }

}
