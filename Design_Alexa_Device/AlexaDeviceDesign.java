// Consider there are differnt types of alexa devices available. 
// One with audio, one with screen, one with audio and screen. 
// These devices may have a battery or may not. 
// Battery devices will have battery percentage. 
// Both battery and non battery devices can be put charging. 
// The task is to show the battery percentage. 
// Include a show methond and that method should show the current battery percentage if it has a battery. 
// If not just say, battery not available. You should also say whether its currently charging or not. 
// There will four statements to print show method like Charging and battery percentage, charging and no battery, just battery percent and no battery.
// Expectation is to write interface-driven code using appropriate design patterns
public class AlexaDeviceDesign {

    // ================= Battery Strategy =================

    interface Battery {
        boolean hasBattery();
        String display();
    }

    // Battery implementation (has battery)
    static class BatteryPowered implements Battery {
        private int percent; // 0 - 100

        public BatteryPowered(int percent) {
            this.percent = percent;
        }

        @Override
        public boolean hasBattery() {
            return true;
        }

        @Override
        public String display() {
            return "Battery: " + percent + "%";
        }
    }

    // No battery implementation
    static class NoBattery implements Battery {
        @Override
        public boolean hasBattery() {
            return false;
        }

        @Override
        public String display() {
            return "Battery: not available";
        }
    }

    // ================= Device Abstraction =================

    interface AlexaDevice {
        String show();
    }

    // Abstract base class (shared logic)
    static abstract class BaseAlexaDevice implements AlexaDevice {
        protected final String name;
        protected final Battery battery;
        protected boolean charging;

        protected BaseAlexaDevice(String name, Battery battery, boolean charging) {
            this.name = name;
            this.battery = battery;
            this.charging = charging;
        }

        @Override
        public String show() {
            return String.format(
                "%s | Charging: %s | %s",
                name,
                charging ? "yes" : "no",
                battery.display()
            );
        }
    }

    // ================= Concrete Devices =================

    static class AudioAlexa extends BaseAlexaDevice {
        public AudioAlexa(Battery battery, boolean charging) {
            super("Audio Alexa", battery, charging);
        }
    }

    static class ScreenAlexa extends BaseAlexaDevice {
        public ScreenAlexa(Battery battery, boolean charging) {
            super("Screen Alexa", battery, charging);
        }
    }

    static class AudioScreenAlexa extends BaseAlexaDevice {
        public AudioScreenAlexa(Battery battery, boolean charging) {
            super("Audio + Screen Alexa", battery, charging);
        }
    }

    // ================= Factory =================

    enum DeviceType {
        AUDIO,
        SCREEN,
        AUDIO_SCREEN
    }

    static class AlexaFactory {
        public static AlexaDevice create(
                DeviceType type,
                boolean hasBattery,
                int batteryPercent,
                boolean charging
        ) {
            Battery battery = hasBattery
                    ? new BatteryPowered(batteryPercent)
                    : new NoBattery();

            switch (type) {
                case AUDIO:
                    return new AudioAlexa(battery, charging);
                case SCREEN:
                    return new ScreenAlexa(battery, charging);
                case AUDIO_SCREEN:
                    return new AudioScreenAlexa(battery, charging);
                default:
                    throw new IllegalArgumentException("Unknown device type");
            }

        }
    }

    // ================= Demo =================

    public static void main(String[] args) {

        AlexaDevice d1 = AlexaFactory.create(
                DeviceType.AUDIO, true, 80, true);

        AlexaDevice d2 = AlexaFactory.create(
                DeviceType.SCREEN, false, 0, true);

        AlexaDevice d3 = AlexaFactory.create(
                DeviceType.AUDIO_SCREEN, true, 45, false);

        AlexaDevice d4 = AlexaFactory.create(
                DeviceType.AUDIO, false, 0, false);

        System.out.println(d1.show());
        System.out.println(d2.show());
        System.out.println(d3.show());
        System.out.println(d4.show());
    }
}
