// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.monitor;

import ole.pstros.utils.ListItem;
import java.util.Vector;

public class ClassManager
{
    private static final boolean DEBUG = false;
    public static final String DEFAULT_PROFILE_NAME = "--Default--";
    private static ClassManager instance;
    private Vector monitors;
    private int latestItemCount;
    private Vector profiles;
    private MonitorProfile currentProfile;
    
    public static ClassManager getInstance() {
        if (ClassManager.instance == null) {
            ClassManager.instance = new ClassManager();
        }
        return ClassManager.instance;
    }
    
    private ClassManager() {
        this.monitors = new Vector();
        this.profiles = new Vector();
        this.currentProfile = new MonitorProfile("--Default--");
        this.profiles.add(this.currentProfile);
        this.currentProfile = null;
    }
    
    public void addClass(final Class cl) {
        final ClassMonitor cm = new ClassMonitor(cl);
        this.monitors.add(cm);
    }
    
    private MonitorProfile getProfile(final String profileName) {
        for (int size = this.profiles.size(), i = 0; i < size; ++i) {
            final MonitorProfile profile = (MonitorProfile)this.profiles.get(i);
            if (profileName.equals(profile.getName())) {
                return profile;
            }
        }
        return null;
    }
    
    public void addVariable(final String variableName, String profileName) {
        if (profileName == null) {
            profileName = "--Default--";
        }
        MonitorProfile profile = this.getProfile(profileName);
        if (profile == null) {
            profile = new MonitorProfile(profileName);
            this.profiles.add(profile);
        }
        profile.addItem(variableName);
    }
    
    public void deleteVariable(final String variableName, String profileName) {
        if (profileName == null) {
            profileName = "--Default--";
        }
        final MonitorProfile profile = this.getProfile(profileName);
        profile.deleteItem(variableName);
    }
    
    public String getCurrentProfileName() {
        if (this.currentProfile == null) {
            return "--Default--";
        }
        return this.currentProfile.getName();
    }
    
    public void invalidateCurrentProfile() {
        this.currentProfile = null;
    }
    
    public String[] getProfileNames() {
        final int size = this.profiles.size();
        final String[] result = new String[size];
        for (int i = 0; i < size; ++i) {
            //result[i] = this.profiles.get(i).getName();
            result[i] = this.profiles.get(i).toString();
        }
        return result;
    }
    
    public MonitorProfile[] getProfiles() {
        final int size = this.profiles.size();
        final MonitorProfile[] result = new MonitorProfile[size];
        for (int i = 0; i < size; ++i) {
            result[i] = (MonitorProfile)this.profiles.get(i);
        }
        return result;
    }
    
    public void setProfile(String profileName, final Object objectRoot) {
        if (profileName == null) {
            profileName = "--Default--";
        }
        MonitorProfile profile = this.getProfile(profileName);
        if (profile == null) {
            profileName = "--Default--";
            profile = this.getProfile(profileName);
        }
        if (this.currentProfile == profile) {
            return;
        }
        this.cleanWatchedVariables();
        this.currentProfile = profile;
        final int size = this.monitors.size();
        Class objectClass = null;
        final ListItem[] result = null;
        if (objectRoot == null) {
            return;
        }
        objectClass = objectRoot.getClass();
        final ClassMonitor cm = this.getClassMonitor(objectClass);
        if (cm != null) {
            for (int profileSize = profile.getSize(), j = 0; j < profileSize; ++j) {
                cm.addWatchedVariable(profile.getItem(j), 0);
            }
        }
    }
    
    private void cleanWatchedVariables() {
        for (int size = this.monitors.size(), i = 0; i < size; ++i) {
            final ClassMonitor cm = (ClassMonitor)this.monitors.get(i);
            cm.cleanWatchedVariables();
        }
    }
    
    public ListItem[] getItems(final Object object) {
        final int size = this.monitors.size();
        Class objectClass = null;
        ListItem[] result = null;
        if (object != null) {
            objectClass = object.getClass();
        }
        for (int i = 0; i < size; ++i) {
            final ClassMonitor cm = (ClassMonitor)this.monitors.get(i);
            if ((object != null && objectClass == cm.getBaseClass()) || object == null) {
                result = cm.getVariables(object);
                this.latestItemCount = cm.getVariableCount();
            }
        }
        return result;
    }
    
    public int getItemCount() {
        return this.latestItemCount;
    }
    
    public ClassMonitor getClassMonitor(Class cl) {
        final int size = this.monitors.size();
        if (cl.isArray()) {
            cl = cl.getComponentType();
        }
        for (int i = 0; i < size; ++i) {
            final ClassMonitor cm = (ClassMonitor)this.monitors.get(i);
            if (cm.getBaseClass() == cl) {
                return cm;
            }
        }
        final ClassMonitor cm = new ClassMonitor(cl);
        this.monitors.add(cm);
        return cm;
    }
}
