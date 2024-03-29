package com.example.feedback;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;


public class Activity_MarkAllocation extends Activity {
    private int indexOfProject;
    private GridView gridView;
    //private ListView listView;
    private ProjectInfo project;
    private ArrayList<Criteria> markedCriteriaList;
    ArrayList<Criteria> allCriteriaList;
    private int markedCriteriaNum;
    private ExpandableListView expandableListView;
    private ExpandableListView eList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__mark_allocation);

        Intent intent = getIntent();
        indexOfProject = Integer.parseInt(intent.getStringExtra("index"));

        init();
    }


    public void init() {
        project = AllFunctions.getObject().getProjectList().get(indexOfProject);
        markedCriteriaList = project.getCriteria();
        markedCriteriaNum = project.getCriteria().size();
        allCriteriaList = new ArrayList<>();
        allCriteriaList.addAll(markedCriteriaList);
        allCriteriaList.addAll(project.getCommentList());

        MyAdapter myAdapter = new MyAdapter(allCriteriaList, this);
        gridView = findViewById(R.id.gridView_CriteriaList_markAllocation);
        gridView.setAdapter(myAdapter);
        //  listView = findViewById(R.id.listView_criteriaList_markAllocation);
        //  listView.setAdapter(myAdapter);
        TextView textView_projectName = findViewById(R.id.textView_projectName_markAllocation);
        textView_projectName.setText(project.getProjectName());
        TextView textView_helloUser = findViewById(R.id.textView_helloUser_markAllocation);
        textView_helloUser.setText("Hello, " + AllFunctions.getObject().getUsername());
        TextView textView_logout = findViewById(R.id.textView_logout_markAllocation);
        textView_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_MarkAllocation.this, Activity_Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    //button 'save'.
    public void save_markAllocation(View view) {
        AllFunctions.getObject().projectCriteria(project, project.getCriteria(), project.getCommentList());
        Intent intent = new Intent(this, Assessment_Preparation_Activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    //button 'back'.
    public void back_markAllocation(View view) {
//        Intent intent = new Intent(this, Activity_CriteriaList.class);
//        intent.putExtra("index", String.valueOf(indexOfProject));
//        startActivity(intent);
        finish();
    }

    //button 'next'.
    public void next_markAllocation(View view) {
        AllFunctions.getObject().projectCriteria(project, project.getCriteria(), project.getCommentList());
        Intent intent = new Intent(this, Activity_Student_Group.class);
        intent.putExtra("index", String.valueOf(indexOfProject));
        startActivity(intent);
    }


    public class MyAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<Criteria> criteriaList;

        public MyAdapter(ArrayList<Criteria> criteriaList, Context context) {
            this.criteriaList = criteriaList;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return criteriaList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
                if (position < markedCriteriaNum) {
                    if(convertView == null)
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_markallocation, parent, false);

                    TextView textView_criteriaName = convertView.findViewById(R.id.textView_criteriaName_gridItem);
                    textView_criteriaName.setText(criteriaList.get(position).getName());
                    EditText editText_maxMark = convertView.findViewById(R.id.editText_maxMark_gridItem);
                    editText_maxMark.setText(String.valueOf(criteriaList.get(position).getMaximunMark()));
                    String markIncrement = criteriaList.get(position).getMarkIncrement();
                    if (markIncrement != null)
                        switch (markIncrement) {
//                case "quarter":
                            case "1/4":
                                RadioButton radioButton_quarter = convertView.findViewById(R.id.radioButton_quarter_gridItem);
                                radioButton_quarter.setChecked(true);
                                break;
//                case "half":
                            case "1/2":
                                RadioButton radioButton_half = convertView.findViewById(R.id.radioButton_half_gridItem);
                                radioButton_half.setChecked(true);
                                break;
//                case "full":
                            case "1":
                                RadioButton radioButton_full = convertView.findViewById(R.id.radioButton_full_gridItem);
                                radioButton_full.setChecked(true);
                                break;
                            default:
                                break;
                        }


                    RadioGroup radioGroup = convertView.findViewById(R.id.radioGroup_markIncrement_gridItem);
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup rG, int checkID) {
                            switch (checkID) {
                                case R.id.radioButton_quarter_gridItem:
//                            criteriaList.get(position).setMarkIncrement("quarter");
                                    markedCriteriaList.get(position).setMarkIncrement("1/4");
                                    break;
                                case R.id.radioButton_half_gridItem:
//                            criteriaList.get(position).setMarkIncrement("half");
                                    markedCriteriaList.get(position).setMarkIncrement("1/2");
                                    break;
                                case R.id.radioButton_full_gridItem:
//                            criteriaList.get(position).setMarkIncrement("full");
                                    markedCriteriaList.get(position).setMarkIncrement("1");
                                    break;
                                default:
                                    markedCriteriaList.get(position).setMarkIncrement("0");
                                    break;
                            }
                        }
                    });

                    Button button_plus = convertView.findViewById(R.id.button_plus_gridItem);
                    button_plus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int mark = Integer.parseInt(editText_maxMark.getText().toString());
                            markedCriteriaList.get(position).setMaximunMark(mark + 1);
                            editText_maxMark.setText(String.valueOf(mark + 1));
                        }
                    });

                    Button button_minus = convertView.findViewById(R.id.button_minus_gridItem);
                    button_minus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int mark = Integer.parseInt(editText_maxMark.getText().toString());
                            if(mark > 0) {
                                markedCriteriaList.get(position).setMaximunMark(mark - 1);
                                editText_maxMark.setText(String.valueOf(mark - 1));
                            }
                        }
                    });


                    Button button_commentDetail = convertView.findViewById(R.id.button_commentsDetail_gridItem);
                    button_commentDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Activity_MarkAllocation.this, Activity_showComment_markAllocation.class);
                            intent.putExtra("indexOfProject",String.valueOf(indexOfProject));
                            intent.putExtra("indexOfCriteria",String.valueOf(position));
                            startActivity(intent);
                        }
                    });
                } else {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_commentonly, parent, false);

                    TextView textView_criteriaName = convertView.findViewById(R.id.textView_criteriaName_gridItemCommentOnly);
                    textView_criteriaName.setText(criteriaList.get(position).getName());

                    Button button_commentDetail = convertView.findViewById(R.id.button_showComments_gridItemCommentOnly);
                    button_commentDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Activity_MarkAllocation.this, Activity_showComment_markAllocation.class);
                            intent.putExtra("indexOfProject",String.valueOf(indexOfProject));
                            intent.putExtra("indexOfCriteria",String.valueOf(position));
                            startActivity(intent);
                        }
                    });
                }

            return convertView;
        }
    }


}
