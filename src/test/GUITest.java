package test;
import exceptions.UsedPortException;
import gui.GUI;
import org.junit.Assert;
import org.junit.Test;

import exceptions.GivenParameterNotExistingException;
import exceptions.PortOutOfBoundException;
import src.ConfigManager;
import src.Configuration;

import javax.swing.*;

import java.awt.event.ActionEvent;

import static org.junit.Assert.assertEquals;

public class GUITest {
    @Test
    public void clickStartButton() throws  PortOutOfBoundException, UsedPortException{
        GUI g=new GUI();
        JButton j=new JButton();
        g.startServerButton.doClick();
        assertEquals("Server is running on port 8080 ",g.label.getText());
    }
    @Test
    public void clickMaintenanceButton() throws  PortOutOfBoundException, UsedPortException{
        GUI g=new GUI();
        JButton j=new JButton();
        g.maintenanceServerButton.doClick();
        assertEquals("Server is in maintenance ",g.label.getText());
    }
    @Test
    public void GUI() throws  PortOutOfBoundException, UsedPortException{
        GUI g=new GUI();
        JButton j=new JButton();
        g.stopServerButton.doClick();
        assertEquals("Server is stopped ",g.label.getText());
    }
}
