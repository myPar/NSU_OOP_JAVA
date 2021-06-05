package GUI;

import Factory.Factory;
import FactoryObjects.FactoryObject;
import FactoryObjects.ObjectsFactory;
import Shop.CarShop;
import Storage.Storage;
import Storage.CarStorage;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class GUI extends Thread {
    private static class SliderHandler implements ChangeListener {
        private JLabel label;
        private JSlider slider;
        private String message;
        private DeltaTimeManager timeManager;

        SliderHandler(JLabel label, JSlider slider, DeltaTimeManager timeManager, String message) {
            this.slider = slider;
            this.label = label;
            this.message = message;
            this.timeManager = timeManager;
        }
        @Override
        public void stateChanged(ChangeEvent changeEvent) {
            int value = slider.getValue();
            label.setText(message + " " + value);
            timeManager.setDeltaTime(value * 1000);
        }
    }
    // GUI display delta-time ms
    private final int deltaTime = 10;
// data references:
    // storage's:
    private Storage motorStorage;
    private Storage bodyStorage;
    private Storage accessoryStorage;
    private CarStorage carStorage;
    // graphic fields:
    // main graphic frame
    private JFrame frame;
    private final int width = 1200;
    private final int height = 1000;
    // data panels:
    private JPanel storagePanel;
    private JPanel objectFactoryPanel;
    private JPanel carSoldPanel;
    // slider panels
    private JPanel sliderPanel1;
    private JPanel sliderPanel2;
    private JPanel sliderPanel3;
    private JPanel sliderPanel4;
    // labels:
    private JLabel motorStorageLabel;
    private JLabel bodyStorageLabel;
    private JLabel accessoryStorageLabel;
    private JLabel carStorageLabel;

    private JLabel motorCreatedLabel;
    private JLabel bodyCreatedLabel;
    private JLabel accessoryCreatedLabel;
    private JLabel carCreatedLabel;

    private JLabel carSoldLabel;
    // sliders:
    private JSlider motorFactorySlider;
    private JSlider bodyFactorySlider;
    private JSlider accessoryFactorySlider;
    private JSlider shopSlider;
// constructor
    public GUI(Storage motorStorage, Storage bodyStorage, Storage accessoryStorage, CarStorage carStorage, Factory motorFactory, Factory bodyFactory, Factory accessoryFactory, CarShop shop) {
        assert  motorStorage.getType() == FactoryObject.Type.MOTOR;
        assert  bodyStorage.getType() == FactoryObject.Type.BODY;
        assert  accessoryStorage.getType() == FactoryObject.Type.ACCESSORY;
        // init storage's
        this.motorStorage = motorStorage;
        this.bodyStorage = bodyStorage;
        this.accessoryStorage = accessoryStorage;
        this.carStorage = carStorage;
        // init graphic
        // create frame
        frame = new JFrame();
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // init panel's sizes:
        storagePanel = new JPanel();
        objectFactoryPanel = new JPanel();
        carSoldPanel = new JPanel();

        sliderPanel1 = new JPanel();
        sliderPanel2 = new JPanel();
        sliderPanel3 = new JPanel();
        sliderPanel4 = new JPanel();

        frame.add(storagePanel);
        frame.add(objectFactoryPanel);
        frame.add(carSoldPanel);

        frame.add(sliderPanel1);
        frame.add(sliderPanel2);
        frame.add(sliderPanel3);
        frame.add(sliderPanel4);

        storagePanel.setBounds(0,0, 170, 100);
        objectFactoryPanel.setBounds(200, 0, 210, 150);
        carSoldPanel.setBounds(0, 200, 110, 50);

        sliderPanel1.setBounds(160, 200, 200, 80);
        sliderPanel2.setBounds(160, 300, 200, 80);
        sliderPanel3.setBounds(160, 400, 200, 80);
        sliderPanel4.setBounds(160, 500, 200, 80);
        // init labels
        motorStorageLabel = new JLabel();
        bodyStorageLabel = new JLabel();
        accessoryStorageLabel = new JLabel();
        carStorageLabel = new JLabel();

        motorCreatedLabel = new JLabel();
        bodyCreatedLabel = new JLabel();
        accessoryCreatedLabel = new JLabel();
        carCreatedLabel = new JLabel();

        carSoldLabel = new JLabel();
        // init sliders
        motorFactorySlider = new JSlider(1, 10, 1);
        bodyFactorySlider = new JSlider(1, 10, 1);
        accessoryFactorySlider = new JSlider(1, 10, 1);
        shopSlider = new JSlider(1, 10, 1);

        Dimension dim = new Dimension(120, 45);
        motorFactorySlider.setPreferredSize(dim);
        bodyFactorySlider.setPreferredSize(dim);
        accessoryFactorySlider.setPreferredSize(dim);
        shopSlider.setPreferredSize(dim);

        motorFactorySlider.setPaintTicks(true);
        bodyFactorySlider.setPaintTicks(true);
        accessoryFactorySlider.setPaintTicks(true);
        shopSlider.setPaintTicks(true);

        motorFactorySlider.setMajorTickSpacing(1);
        bodyFactorySlider.setMajorTickSpacing(1);
        accessoryFactorySlider.setMajorTickSpacing(1);
        shopSlider.setMajorTickSpacing(1);

        motorFactorySlider.setPaintLabels(true);
        bodyFactorySlider.setPaintLabels(true);
        accessoryFactorySlider.setPaintLabels(true);
        shopSlider.setPaintLabels(true);

        sliderPanel1.add(motorFactorySlider);
        sliderPanel2.add(bodyFactorySlider);
        sliderPanel3.add(accessoryFactorySlider);
        sliderPanel4.add(shopSlider);

        JLabel label1 = new JLabel();
        label1.setText("Motor Factory dt");
        sliderPanel1.add(label1);

        JLabel label2 = new JLabel();
        label2.setText("Body Factory dt");
        sliderPanel2.add(label2);

        JLabel label3 = new JLabel();
        label3.setText("Accessory Factory dt");
        sliderPanel3.add(label3);

        JLabel label4 = new JLabel();
        label4.setText("Shop dt");
        sliderPanel4.add(label4);

        motorFactorySlider.addChangeListener(new SliderHandler(label1, motorFactorySlider, motorFactory.getTimeManager(), "Motor Factory dt"));
        bodyFactorySlider.addChangeListener(new SliderHandler(label2, bodyFactorySlider, bodyFactory.getTimeManager(), "Body Factory dt"));
        accessoryFactorySlider.addChangeListener(new SliderHandler(label3, accessoryFactorySlider, accessoryFactory.getTimeManager(), "Accessory Factory dt"));
        shopSlider.addChangeListener(new SliderHandler(label4, shopSlider, shop.getTimeManager(), "Shop dt"));

        storagePanel.add(motorStorageLabel);
        storagePanel.add(bodyStorageLabel);
        storagePanel.add(accessoryStorageLabel);
        storagePanel.add(carStorageLabel);

        objectFactoryPanel.add(motorCreatedLabel);
        objectFactoryPanel.add(bodyCreatedLabel);
        objectFactoryPanel.add(accessoryCreatedLabel);
        objectFactoryPanel.add(carCreatedLabel);

        carSoldPanel.add(carSoldLabel);
        frame.setVisible(true);
    }

    private void viewGUI() {
        int[] prevCreatedCount = {0, 0, 0, 0};
        int[] prevStorageCount = {0, 0, 0, 0};
        int prevCarSoldCount = 0;

        while(true) {
            try {
                // update object factory panel
                int[] counts = ObjectsFactory.getCounts();
                if (counts[0] != prevCreatedCount[0]) {
                    motorCreatedLabel.setText("Motors Created: " + counts[0]);
                    motorCreatedLabel.repaint();
                    prevCreatedCount[0] = counts[0];
                }
                if (counts[1] != prevCreatedCount[1]) {
                    bodyCreatedLabel.setText("Body Created: " + counts[1]);
                    bodyCreatedLabel.repaint();
                    prevCreatedCount[1] = counts[1];
                }
                if (counts[2] != prevCreatedCount[2]) {
                    accessoryCreatedLabel.setText("Accessory Created: " + counts[2]);
                    accessoryCreatedLabel.repaint();
                    prevCreatedCount[2] = counts[2];
                }
                if (counts[3] != prevCreatedCount[3]) {
                    carCreatedLabel.setText("Car Created: " + counts[3]);
                    carCreatedLabel.repaint();
                    prevCreatedCount[3] = counts[3];
                }
                //objectFactoryPanel.repaint();

                // update storage panel
                int motorCount = motorStorage.getCount();
                int bodyCount = bodyStorage.getCount();
                int accessoryCount = accessoryStorage.getCount();
                int carCount = carStorage.getCount();

                if (prevStorageCount[0] != motorCount) {
                    motorStorageLabel.setText("Motors in storage: " + motorCount);
                    motorStorageLabel.repaint();
                    prevStorageCount[0] = motorCount;
                }
                if (prevStorageCount[1] != bodyCount) {
                    bodyStorageLabel.setText("Body in storage: " + bodyCount);
                    bodyStorageLabel.repaint();
                    prevStorageCount[1] = bodyCount;
                }
                if (prevStorageCount[2] != accessoryCount) {
                    accessoryStorageLabel.setText("Accessory in storage: " + accessoryCount);
                    accessoryStorageLabel.repaint();
                    prevStorageCount[2] = accessoryCount;
                }
                if (prevStorageCount[3] != carCount) {
                    carStorageLabel.setText("Cars in storage: " + carCount);
                    carStorageLabel.repaint();
                    prevStorageCount[3] = carCount;
                }
                //storagePanel.repaint();

                // update car sanded panel
                int carSoldCount = carStorage.getSandedCount();
                if (prevCarSoldCount != carSoldCount) {
                    carSoldLabel.setText("Cars sold: " + carSoldCount);
                    carSoldLabel.repaint();
                    prevCarSoldCount = carSoldCount;
                }
                //carSoldPanel.repaint();
//                frame.repaint();

                Thread.sleep(deltaTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
    @Override
    public void run() {
        viewGUI();
    }
}
