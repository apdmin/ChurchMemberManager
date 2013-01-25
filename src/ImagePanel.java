/*
  Andrew Darwin
  www.adarwin.com
  January 2013
*/

package com.adarwin.pcbc;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ImagePanel extends JPanel
{
  public ImagePanel()
  {
    setBorder(BorderFactory.createEtchedBorder());
    setPreferredSize(new Dimension(200, 200));
  }
}
