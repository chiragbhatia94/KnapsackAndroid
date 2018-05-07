package com.chiragbhatia.scimataknapsack;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class ProblemSolutionActivity extends AppCompatActivity {

    private static final String TAG = "ProblemSolutionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_solution);

        final EditText itemsET = findViewById(R.id.itemsET);
        final EditText capacityET = findViewById(R.id.capacityET);

        final Button generateRandomInputsButton = findViewById(R.id.generateRandomInputsButton);
        final Button addManualInputsButton = findViewById(R.id.addManualInputsButton);

        final ListView itemsListView = findViewById(R.id.itemsListView);

        final LinkedHashMap<Integer, Integer> itemsMap = new LinkedHashMap<>();
        final ArrayList<String> itemStrings = new ArrayList<>();

        generateRandomInputsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String capacityString = capacityET.getText().toString();
                if (capacityString.isEmpty()) {
                    Toast.makeText(ProblemSolutionActivity.this, R.string.enter_valid_capacity, Toast.LENGTH_LONG).show();
                    return;
                }
                int capacity = Integer.parseInt(capacityString);

                String itemString = itemsET.getText().toString();
                if (itemString.isEmpty()) {
                    Toast.makeText(ProblemSolutionActivity.this, R.string.enter_valid_no_of_items, Toast.LENGTH_LONG).show();
                    return;
                }
                int noOfItems = Integer.parseInt(itemString);
                if (noOfItems < 1) {
                    Toast.makeText(ProblemSolutionActivity.this, R.string.enter_valid_no_of_items, Toast.LENGTH_LONG).show();
                    return;
                }

                int[] values = new int[noOfItems];
                int[] weights = new int[noOfItems];
                int[][] S;

                boolean runRandom = true;

                if (runRandom)
                    for (int i = 0; i < noOfItems; i++) {
                        Random random = new Random();
                        int randomWeight = 1 + random.nextInt(capacity);
                        int randomValue = 1 + random.nextInt(10);

                        weights[i] = randomWeight;
                        values[i] = randomValue;

                        itemStrings.add(getString(R.string.item) + " " + i + ": Weight(" + randomWeight + ") Value(" + randomValue + ")");
                    }
                else {
                    // this is just for testing
                    noOfItems = 3;
                    capacity = 5;
                    weights = new int[]{4, 2, 3};
                    values = new int[]{10, 4, 7};
                }

                System.out.println("Weights: " + Arrays.toString(weights));
                System.out.println("Values: " + Arrays.toString(values));

                S = solveKnapsack(weights, values, noOfItems, capacity);
                System.out.println("Total Benefit: " + S[noOfItems][capacity]);

                int[] selected = findItemsToTake(weights, noOfItems, capacity, S);
                System.out.println(Arrays.toString(selected));

                generateRandomInputsButton.setVisibility(View.GONE);
                addManualInputsButton.setVisibility(View.GONE);
                capacityET.setEnabled(false);
                itemsET.setEnabled(false);

                System.out.println(itemStrings);

                itemsListView.setAdapter(new ArrayAdapter<String>(ProblemSolutionActivity.this, android.R.layout.simple_list_item_1, itemStrings));

                Toast.makeText(ProblemSolutionActivity.this, "Max profit will be " + S[noOfItems][capacity] + "\n" + Arrays.toString(selected), Toast.LENGTH_SHORT).show();
            }

        });

        addManualInputsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String capacityString = capacityET.getText().toString();
                if (capacityString.isEmpty()) {
                    Toast.makeText(ProblemSolutionActivity.this, R.string.enter_valid_capacity, Toast.LENGTH_LONG).show();
                    return;
                }
                final int capacity = Integer.parseInt(capacityString);

                String itemString = itemsET.getText().toString();
                if (itemString.isEmpty()) {
                    Toast.makeText(ProblemSolutionActivity.this, R.string.enter_valid_no_of_items, Toast.LENGTH_LONG).show();
                    return;
                }
                final int noOfItems = Integer.parseInt(itemString);
                if (noOfItems < 1) {
                    Toast.makeText(ProblemSolutionActivity.this, R.string.enter_valid_no_of_items, Toast.LENGTH_LONG).show();
                    return;
                }

                Log.i(TAG, "onClick: " + noOfItems);

                final Temp t = new Temp(noOfItems);

                for (int i = 1; i <= noOfItems; i++) {
                    Context context = addManualInputsButton.getContext();
                    LinearLayout layout = new LinearLayout(context);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    // Add a TextView here for the "Weight" label, as noted in the comments
                    final EditText weightBox = new EditText(context);
                    weightBox.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                    weightBox.setHint(R.string.weight);

                    layout.addView(weightBox);

                    // Add another TextView here for the "Value" label
                    final EditText valueBox = new EditText(context);
                    valueBox.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                    valueBox.setHint(R.string.value);
                    layout.addView(valueBox);

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProblemSolutionActivity.this);
                    alertDialogBuilder.setTitle(R.string.add_item_title)
                            .setPositiveButton(R.string.next, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    int weight = Integer.parseInt(weightBox.getText().toString());
                                    int value = Integer.parseInt(valueBox.getText().toString());

                                    int temp = itemStrings.size() + 1;
                                    itemsMap.put(weight, value);
                                    itemStrings.add(getString(R.string.item) + " " + temp + ": Weight(" + weight + ") Value(" + value + ")");

                                    t.subA();
                                }
                            })
                            .setView(layout)
                            .setCancelable(false);
                    final AlertDialog dialog = alertDialogBuilder.create();

                    weightBox.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence c, int i, int i2, int i3) {
                        }

                        @Override
                        public void onTextChanged(CharSequence c, int i, int i2, int i3) {
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            // Will be called AFTER text has been changed.
                            if (editable.toString().length() == 0) {
                                valueBox.setEnabled(false);
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                            } else {
                                valueBox.setEnabled(true);
                            }
                        }
                    });

                    valueBox.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence c, int i, int i2, int i3) {
                        }

                        @Override
                        public void onTextChanged(CharSequence c, int i, int i2, int i3) {
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            // Will be called AFTER text has been changed.
                            if (editable.toString().length() == 0) {
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                            } else {
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                            }
                        }
                    });

                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (t.getA() == 0) {
                                System.out.println("This was fun");
                                int weights[] = new int[noOfItems];
                                int values[] = new int[noOfItems];
                                int[][] S;
                                int k = 0;
                                for (Map.Entry<Integer, Integer> entry : itemsMap.entrySet()) {
                                    weights[k] = entry.getKey();
                                    values[k] = entry.getValue();
                                    k++;
                                }

                                System.out.println("NoOfItems: " + noOfItems);
                                System.out.println("Capacity: " + capacity);
                                System.out.println("Weights: " + Arrays.toString(weights));
                                System.out.println("Values : " + Arrays.toString(values));

                                // here perform knapsack solution

                                System.out.println("Weights: " + Arrays.toString(weights));
                                System.out.println("Values: " + Arrays.toString(values));

                                S = solveKnapsack(weights, values, noOfItems, capacity);
                                System.out.println("Total Benefit: " + S[noOfItems][capacity]);

                                int[] selected = findItemsToTake(weights, noOfItems, capacity, S);

                                Toast.makeText(ProblemSolutionActivity.this, "Max profit will be " + S[noOfItems][capacity] + "\n" + Arrays.toString(selected), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    dialog.show();

                    // The buttons are initially deactivated, as the fields are initially empty:
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    valueBox.setEnabled(false);
                }

                itemsListView.setAdapter(new ArrayAdapter<>(ProblemSolutionActivity.this, android.R.layout.simple_list_item_1, itemStrings));
                generateRandomInputsButton.setVisibility(View.GONE);
                addManualInputsButton.setVisibility(View.GONE);
                capacityET.setEnabled(false);
                itemsET.setEnabled(false);
            }
        });
    }

    public static int[][] solveKnapsack(int wi[], int vi[], int n, int W) {
        int S[][] = new int[n + 1][W + 1];
        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= W; w++) {
                if (w - wi[i - 1] >= 0)
                    S[i][w] = Math.max(S[i - 1][w], vi[i - 1] + S[i - 1][w - wi[i - 1]]);
                else
                    S[i][w] = S[i - 1][w];
            }
        }

        System.out.println("S: w");
        System.out.println("i");

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= W; j++) {
                System.out.print(S[i][j] + "\t");
            }
            System.out.println();
        }
        return S;
    }

    public static int[] findItemsToTake(int wi[], int noOfItems, int capacity, int S[][]) {
        int selected[] = new int[noOfItems];

        for (int n = noOfItems, w = capacity; n > 0; n--) {
            if (S[n][w] != 0 && S[n][w] != S[n - 1][w]) {
                // System.out.println("We select item no: " + n);
                selected[n - 1] = 1;
                w = w - wi[n - 1];
            }
        }

        return selected;
    }

    class Temp {
        private int a;

        Temp(int noOfTimes) {
            a = noOfTimes;
        }

        public int getA() {
            return a;
        }

        public void subA() {
            this.a--;
            System.out.println("subing a");
        }

        public void setA(int a) {
            this.a = a;
        }
    }
}
