// 
// Decompiled by Procyon v0.5.36
// 

package com.gcjsp.v10.system.device;

public class DeviceControl
{
    public static final int BATTERY = 1;
    public static final int FIELD_INTENSITY = 2;
    public static final int KEY_STATE = 3;
    public static final int VIBRATION = 4;
    public static final int BACK_LIGHT = 5;
    public static final int EIGHT_DIRECTIONS = 6;
    private static DeviceControl instance;
    private int deviceState;
    static MailListener mailListener;
    static RingStateListener ringStateListener;
    static ScheduledAlarmListener scheduledAlarmListener;
    static TelephonyListener telephonyListener;
    
    public static final DeviceControl getDefaultDeviceControl() {
        if (DeviceControl.instance == null) {
            DeviceControl.instance = new DeviceControl();
        }
        return DeviceControl.instance;
    }
    
    public int getDeviceState(final int deviceNo) {
        switch (deviceNo) {
            case 1: {
                return 100;
            }
            case 2: {
                return 100;
            }
            case 3: {
                return this.getKeyState();
            }
            default: {
                throw new IllegalStateException();
            }
        }
    }
    
    private int getKeyState() {
        return 0;
    }
    
    public boolean getKeyRepeatState(final int key) {
        return false;
    }
    
    public boolean setKeyRepeatState(final int key, final boolean state) {
        return false;
    }
    
    public boolean isDeviceActive(final int deviceNo) {
        switch (deviceNo) {
            case 1:
            case 2:
            case 6: {
                return (1 << deviceNo & this.deviceState) != 0x0;
            }
            default: {
                throw new IllegalStateException();
            }
        }
    }
    
    public boolean setDeviceActive(final int deviceNo, final boolean active) {
        switch (deviceNo) {
            case 1:
            case 2:
            case 6: {
                if (active) {
                    this.deviceState |= 1 << deviceNo;
                }
                else {
                    this.deviceState &= ~(1 << deviceNo);
                }
                return true;
            }
            default: {
                throw new IllegalStateException();
            }
        }
    }
    
    public static void setMailListener(final MailListener listener) {
        DeviceControl.mailListener = listener;
    }
    
    public static void setRingStateListener(final RingStateListener listener) {
        DeviceControl.ringStateListener = listener;
    }
    
    public static void setScheduledAlarmListener(final ScheduledAlarmListener listener) {
        DeviceControl.scheduledAlarmListener = listener;
    }
    
    public static void setTelephonyListener(final TelephonyListener listener) {
        DeviceControl.telephonyListener = listener;
    }
}
