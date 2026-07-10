package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LedSub extends SubsystemBase {
  private AddressableLED led;
  private AddressableLEDBuffer ledBuffer;

  private final Distance ledLenth;
  
  private LEDPattern scrollingRainbow;
  private LEDPattern rainbow;
  private LEDPattern solidGreen;

  public LedSub(){
    led = new AddressableLED(0);
    ledBuffer = new AddressableLEDBuffer(100);

    led.setLength(ledBuffer.getLength());
    led.setData(ledBuffer);
    led.start();

    ledLenth = Meters.of(1.0 / 40.0);

    scrollingRainbow = rainbow.scrollAtAbsoluteSpeed(MetersPerSecond.of(3), ledLenth);
    solidGreen = LEDPattern.solid(Color.kGreen);
    
    scrollingRainbow();
  }

  public void scrollingRainbow(){
    scrollingRainbow.applyTo(ledBuffer);
    led.setData(ledBuffer);
  }

  public void solidGreen(){
    solidGreen.applyTo(ledBuffer);
    led.setData(ledBuffer);
  }

  @Override
  public void periodic(){

  }
}