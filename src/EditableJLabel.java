/*
  Andrew Darwin
  www.adarwin.com
  January 2013
*/

package com.adarwin.pcbc;

import com.adarwin.logging.Logbook;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EditableJLabel extends JPanel
{
  private JLabel labelVersion;
  private JTextField textFieldVersion;
  private String originalText;

  public EditableJLabel(String text)
  {
    originalText = text;
    labelVersion = new JLabel(text);
    textFieldVersion = new JTextField(text);
    setLayout(new BorderLayout());

    labelVersion.addMouseListener(new MouseListener()
    {
      public void mouseClicked(MouseEvent e)
      {
        if (e.getClickCount() > 1)
        {
          enterEditMode();
        }
      }
      public void mouseEntered(MouseEvent e) {}
      public void mouseExited(MouseEvent e) {}
      public void mousePressed(MouseEvent e) {}
      public void mouseReleased(MouseEvent e) {}
    });
    textFieldVersion.addFocusListener(new FocusListener()
    {
      public void focusGained(FocusEvent e) {}
      public void focusLost(FocusEvent e)
      {
        setText(labelVersion.getText());
        enterViewMode();
      }
    });
    textFieldVersion.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent e)
      {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
          if (textFieldVersion.getText().equals(""))
          {
            //Revert to previous text
            setText(labelVersion.getText());
          }
          else
          {
            setText(textFieldVersion.getText());
          }
          enterViewMode();
        }
      }
      public void keyReleased(KeyEvent e) {}
      public void keyTyped(KeyEvent e)
      {
      }
    });
    textFieldVersion.getDocument().addDocumentListener(new DocumentListener()
    {
      public void changedUpdate(DocumentEvent e) {}
      public void insertUpdate(DocumentEvent e)
      {
        revalidate();
      }
      public void removeUpdate(DocumentEvent e)
      {
        revalidate();
      }
    });
    add(labelVersion, BorderLayout.CENTER);
  }

  protected void setInitialText(String text)
  {
    originalText = text;
    setText(text);
    labelVersion.setForeground(Color.black);
  }
  public void setText(String text)
  {
    labelVersion.setText(text);
    textFieldVersion.setText(text);
  }
  public String getText()
  {
    return labelVersion.getText();
  }

  public void enterEditMode()
  {
    remove(labelVersion);
    add(textFieldVersion, BorderLayout.CENTER);
    textFieldVersion.requestFocus();
    textFieldVersion.selectAll();
    revalidate();
    repaint();
  }

  public void enterViewMode()
  {
    if (!textFieldVersion.getText().equals(originalText))
    {
      labelVersion.setForeground(Color.red);
      //textFieldVersion.setForeground(Color.red);
    }
    else
    {
      labelVersion.setForeground(Color.black);
      textFieldVersion.setForeground(Color.black);
    }
    remove(textFieldVersion);
    add(labelVersion, BorderLayout.CENTER);
    revalidate();
    repaint();
  }


  public boolean isChanged()
  {
    if (!labelVersion.getText().equals(textFieldVersion.getText()))
    {
      ChurchMemberManager.logbook.log(Logbook.WARNING,
                                      "The label version and text field versions of the editable " +
                                      "JLabel \"" + labelVersion.getText() + "\" do not match.");
    }
    return !labelVersion.getText().equals(originalText);
  }
}
