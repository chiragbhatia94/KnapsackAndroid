package com.chiragbhatia.scimataknapsack;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class ProblemSolutionActivity extends AppCompatActivity {

    private static final String TAG = "PRO";

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
                int items = Integer.parseInt(itemString);
                if (items < 1) {
                    Toast.makeText(ProblemSolutionActivity.this, R.string.enter_valid_no_of_items, Toast.LENGTH_LONG).show();
                    return;
                }
                for (int i = 1; i <= items; i++) {
                    Random random = new Random();
                    int randomWeight = 1 + random.nextInt(10);
                    int randomValue = 1 + random.nextInt(10);

                    itemsMap.put(randomWeight, randomValue);
                    itemStrings.add(getString(R.string.item) + " " + i + ": Weight(" + randomWeight + ") Value(" + randomValue + ")");
                }
                generateRandomInputsButton.setVisibility(View.GONE);
                addManualInputsButton.setVisibility(View.GONE);
                capacityET.setEnabled(false);
                itemsET.setEnabled(false);

                itemsListView.setAdapter(new ArrayAdapter<String>(ProblemSolutionActivity.this, android.R.layout.simple_list_item_1, itemStrings));

                int wt[] = new int[items];
                int vt[] = new int[items];
                int k = 0;
                for (Map.Entry<Integer, Integer> entry : itemsMap.entrySet()) {
                    wt[k] = entry.getKey();
                    vt[k] = entry.getValue();
                    k++;
                }

                int maxProfit = knapsack(capacity, wt, vt, items);
                System.out.println(maxProfit);
                Toast.makeText(ProblemSolutionActivity.this, "Max profit will be " + maxProfit, Toast.LENGTH_SHORT).show();
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
                final int items = Integer.parseInt(itemString);
                if (items < 1) {
                    Toast.makeText(ProblemSolutionActivity.this, R.string.enter_valid_no_of_items, Toast.LENGTH_LONG).show();
                    return;
                }


                Log.i(TAG, "onClick: " + items);

                for (int i = 1; i <= items; i++) {
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

                                    int wt[] = new int[items];
                                    int vt[] = new int[items];
                                    int k = 0;
                                    for (Map.Entry<Integer, Integer> entry : itemsMap.entrySet()) {
                                        wt[k] = entry.getKey();
                                        vt[k] = entry.getValue();
                                        k++;
                                    }
                                    int maxProfit = knapsack(capacity, wt, vt, items);
                                    System.out.println(maxProfit);
                                    Toast.makeText(ProblemSolutionActivity.this, "Max profit will be " + maxProfit, Toast.LENGTH_SHORT).show();
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

                    dialog.show();
                    // The buttons are initially deactivated, as the fields are initially empty:
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    valueBox.setEnabled(false);
                }

                itemsListView.setAdapter(new ArrayAdapter<String>(ProblemSolutionActivity.this, android.R.layout.simple_list_item_1, itemStrings));
                generateRandomInputsButton.setVisibility(View.GONE);
                addManualInputsButton.setVisibility(View.GONE);
                capacityET.setEnabled(false);
                itemsET.setEnabled(false);
            }
        });
    }

    // A utility function that returns maximum of two integers
    static int max(int a, int b) {
        return (a > b) ? a : b;
    }

    // Returns the maximum value that can be put in a knapsack of capacity W
    static int knapsack(int W, int wt[], int val[], int n) {
        int i, w;
        int V[][] = new int[n + 1][W + 1];

        // Build table V[][] in bottom up manner
        for (i = 0; i <= n; i++) {
            for (w = 0; w <= W; w++) {
                if (i == 0 || w == 0) {
                    V[i][w] = 0;
                } else if (wt[i - 1] <= w) {
                    V[i][w] = max(val[i - 1] + V[i - 1][w - wt[i - 1]], V[i - 1][w]);
                } else {
                    V[i][w] = V[i - 1][w];
                }
            }
        }

        return V[n][W];
    }
}
