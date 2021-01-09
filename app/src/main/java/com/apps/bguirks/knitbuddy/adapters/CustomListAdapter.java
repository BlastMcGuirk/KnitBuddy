package com.apps.bguirks.knitbuddy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.apps.bguirks.knitbuddy.R;
import com.apps.bguirks.knitbuddy.database.DatabaseHelper;
import com.apps.bguirks.knitbuddy.database.dataobjects.Instruction;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {

    private Context context;
    private List<Instruction> instructions;

    private DatabaseHelper db;

    public CustomListAdapter(Context context, List<Instruction> instructionList) {
        this.context = context;
        this.instructions = instructionList;

        this.db = new DatabaseHelper(context);
    }

    @Override
    public int getCount() {
        return instructions.size();
    }

    @Override
    public Object getItem(int position) {
        return instructions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return instructions.get(position).get_id();
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            convertView = layoutInflater.inflate(R.layout.instruction_item, null);
        }

        TextView stepNumber = convertView.findViewById(R.id.step_number);
        CheckBox completed = convertView.findViewById(R.id.completed);
        final TextView instruction = convertView.findViewById(R.id.instruction_value);
        TextView instructionEdit = convertView.findViewById(R.id.instruction_edit);
        ImageView dropdown = convertView.findViewById(R.id.dropdown);
        final TextView counter = convertView.findViewById(R.id.counter);
        final Button minusButton = convertView.findViewById(R.id.minus_count_button);
        final Button plusButton = convertView.findViewById(R.id.plus_count_button);

        final Instruction ins = instructions.get(position);
        stepNumber.setText(String.valueOf(ins.getStepNumber()));
        completed.setChecked(ins.getCompleted() == 1);
        instruction.setText(ins.getInstruction());
        instructionEdit.setText(ins.getInstruction());

        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ins.setCompleted(((CheckBox)v).isChecked() ? 1 : 0);
                db.instructions.update(ins);
            }
        });

        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.instruction_popup_menu, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        ViewGroup.LayoutParams params = parent.getLayoutParams();
                        switch (item.getItemId()) {
                            case R.id.toggle_counter:
                                if (ins.getCounter() == -999) {
                                    ins.setCounter(0);
                                    counter.setVisibility(View.VISIBLE);
                                    minusButton.setVisibility(View.VISIBLE);
                                    plusButton.setVisibility(View.VISIBLE);
                                } else {
                                    ins.setCounter(-999);
                                    counter.setVisibility(View.GONE);
                                    minusButton.setVisibility(View.GONE);
                                    plusButton.setVisibility(View.GONE);
                                }
                                db.instructions.update(ins);
                                break;
                            case R.id.delete:
                                db.instructions.delete(ins.get_id());
                                instructions.remove(position);
                                break;
                        }
                        params.height = getInstructionsHeight();
                        parent.setLayoutParams(params);
                        parent.requestLayout();
                        notifyDataSetChanged();
                        return true;
                    }
                });
            }
        });

        if (ins.getCounter() == -999) {
            counter.setVisibility(View.GONE);
            minusButton.setVisibility(View.GONE);
            plusButton.setVisibility(View.GONE);
        } else {
            counter.setText(String.valueOf(ins.getCounter()));
            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ins.setCounter(ins.getCounter() - 1);
                    db.instructions.update(ins);
                    notifyDataSetChanged();
                }
            });
            plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ins.setCounter(ins.getCounter() + 1);
                    db.instructions.update(ins);
                    notifyDataSetChanged();
                }
            });
        }

        return convertView;
    }

    private int getInstructionsHeight() {
        int totalSize = 0;
        for (Instruction ins : instructions) {
            if (ins.getCounter() == -999) {
                totalSize += 180;
            } else {
                totalSize += 415;
            }
        }
        return totalSize;
    }

    public void updateData(List<Instruction> instructions) {
        this.instructions = instructions;
        this.notifyDataSetChanged();
    }
}
