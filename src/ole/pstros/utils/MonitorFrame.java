// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.utils;

import java.awt.event.ItemEvent;
import ole.pstros.MainApp;
import java.awt.ScrollPaneAdjustable;
import java.awt.event.WindowListener;
import java.awt.Container;
import java.awt.ScrollPane;
import java.awt.Component;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.BorderLayout;
import ole.pstros.monitor.ClassManager;
import java.awt.Choice;
import java.awt.event.ItemListener;
import java.awt.Frame;

public class MonitorFrame extends Frame implements ItemListener
{
    private static boolean initialized;
    private Choice profileChoice;
    private ListViewer listViewer;
    
    public void doLayout() {
        if (!MonitorFrame.initialized) {
            this.initComponents();
            MonitorFrame.initialized = true;
        }
        super.doLayout();
    }
    
    private void initComponents() {
        this.setTitle("Monitor");
        final ClassManager cm = ClassManager.getInstance();
        final BorderLayout cl = new BorderLayout();
        final Panel globalPanel = new Panel(cl);
        final Panel profilePanel = new Panel(new BorderLayout());
        profilePanel.add(new Label("Profile"), "West");
        this.profileChoice = new Choice();
        final String[] profiles = cm.getProfileNames();
        for (int i = 0; i < profiles.length; ++i) {
            this.profileChoice.add(profiles[i]);
        }
        this.profileChoice.addItemListener(this);
        profilePanel.add(this.profileChoice, "Center");
        profilePanel.setBackground(ListViewer.COLOR_LIGHT_GRAY);
        globalPanel.add(profilePanel, "North");
        final ScrollPane scrollPane = new ScrollPane(0);
        (this.listViewer = new ListViewer(this, scrollPane)).setColumnInfo(new String[] { "Variable", "Value" }, new int[] { 160, 80 });
        this.addWindowListener(this.listViewer);
        this.listViewer.setVerticalScroll((ScrollPaneAdjustable)scrollPane.getVAdjustable());
        scrollPane.add(this.listViewer);
        globalPanel.add(scrollPane, "Center");
        this.add(globalPanel);
    }
    
    public void updateItems() {
        if (this.listViewer == null || !this.listViewer.isVisible()) {
            return;
        }
        final ClassManager cm = ClassManager.getInstance();
        final ListItem[] items = cm.getItems(MainApp.midlet);
        final int itemCount = cm.getItemCount();
        this.listViewer.setItems(items, itemCount);
        this.listViewer.repaintItems();
    }
    
    public void selectProfileChoice(final String name) {
        this.profileChoice.select(name);
    }
    
    public void itemStateChanged(final ItemEvent e) {
        final ClassManager cm = ClassManager.getInstance();
        cm.setProfile((String)e.getItem(), MainApp.midlet);
        this.listViewer.notifyProfileChange();
    }
    
    public void dispose() {
        this.listViewer.storeBounds();
        MonitorFrame.initialized = false;
        super.dispose();
    }
}
