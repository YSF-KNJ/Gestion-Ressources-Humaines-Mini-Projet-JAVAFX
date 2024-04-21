package com.mycompany.util;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

import java.util.function.UnaryOperator;

public class CustomSpinner extends Spinner<Integer> {

    public CustomSpinner() {
        super();
        this.setEditable(true);
        this.setupTextFormatter();
        this.setRange(0, Integer.MAX_VALUE, 1);
    }

    public void setRange(int min, int max, int initialValue) {
        this.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue));
    }

    private void setupTextFormatter() {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        };
        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), 0, filter);
        this.getEditor().setTextFormatter(textFormatter);

        textFormatter.valueProperty().addListener((obs, oldValue, newValue) -> {
            SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = (SpinnerValueFactory.IntegerSpinnerValueFactory) this.getValueFactory();
            if (newValue != null && (newValue < valueFactory.getMin() || newValue > valueFactory.getMax())) {
                Utils.displayError("La valeur doit être comprise entre " + valueFactory.getMin() + " et " + valueFactory.getMax() + "!");
                textFormatter.setValue(oldValue);
            }
        });
    }

    public int getIntValue() {
        return getValue().intValue();
    }
}