package com.chiragbhatia.scimataknapsack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int[] selected = (int[]) bundle.get(CommonConstants.SELECTED);
        int[] weights = (int[]) bundle.get(CommonConstants.WEIGHTS);
        int[] values = (int[]) bundle.get(CommonConstants.VALUES);

        int capacity = (int) bundle.get(CommonConstants.CAPACITY);
        int maxProfit = (int) bundle.get(CommonConstants.MAX_PROFIT);

        ListView resultsLV = findViewById(R.id.resultsLV);
        TextView finalWeightNValueTV = findViewById(R.id.finalWeightNValueTV);
        TextView knapsackCapacityTV = findViewById(R.id.knapsackCapacityTV);
        TextView noOfSelectedItems = findViewById(R.id.noOfSelectedItems);

        ArrayList<String> itemsList = new ArrayList<>();

        int totalWeight = 0;

        assert selected != null;
        for (int i = 0; i < selected.length; i++) {
            if (selected[i] == 1) {
                assert weights != null;
                assert values != null;
                itemsList.add("#" + (i + 1) + ": Weight(" + weights[i] + ") Value(" + values[i] + ")");
                totalWeight += weights[i];
            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ResultsActivity.this, android.R.layout.simple_list_item_1, itemsList);
        resultsLV.setAdapter(arrayAdapter);

        knapsackCapacityTV.setText("Knapsack Capacity: " + capacity);
        noOfSelectedItems.setText("Following items are selected out of " + (selected.length + 1) + " items.");
        finalWeightNValueTV.setText("Final Weight: " + totalWeight + " Value" + maxProfit);
    }
}
