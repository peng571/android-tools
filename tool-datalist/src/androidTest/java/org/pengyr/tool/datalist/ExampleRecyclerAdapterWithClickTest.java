//package dev.momo.library.connect.data;
//
//import android.content.Context;
//import android.support.test.InstrumentationRegistry;
//import android.support.test.runner.AndroidJUnit4;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import dev.momo.library.core.data.recycler.ListRecyclerAdapter;
//import dev.momo.library.core.data.recycler.ObjectViewHolder;
//import dev.momo.library.core.data.recycler.ViewHolderListener;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static org.junit.Assert.assertEquals;
//
///**
//* Instrumentation test, which will execute on an Android device.
//*
//* @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
//*/
//@RunWith(AndroidJUnit4.class)
//public class ExampleRecyclerAdapterWithClickTest {
//   @Test
//   public void useAppContext() throws Exception {
//       // Context of the app under test.
//       Context appContext = InstrumentationRegistry.getTargetContext();
//
//       assertEquals("android.alchema.com.app", appContext.getPackageName());
//
//
//   }
//
//   class ExampleViewHolder extends ObjectViewHolder<String>{
//
//       public ExampleViewHolder(View itemView) {
//           super(itemView);
//       }
//
//       public void onBind(int index, String object){
//           super.onBind(index, object);
//       }
//
//   }
//
//   ListRecyclerAdapter<String, ExampleViewHolder> adapter = new ListRecyclerAdapter<String, ExampleViewHolder>() {
//
//       ViewHolderListener<String> itemClickListener = new ViewHolderListener<String>() {
//           @Override
//           public void onItemClick(RecyclerView.ViewHolder viewHolder, int index, String object) {
////                Assert()
//           }
//       };
//
//       @Override
//       public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//           LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//           View v = inflater.inflate(/* test layout */ R.layout.recipe_row, parent, false);
//           ExampleViewHolder viewHolder = new ExampleViewHolder(v);
//           viewHolder.listen(itemClickListener);
//           return viewHolder;
//       }
//
//       @Override
//       public void onBindViewHolder(ExampleViewHolder holder, int position) {
//           holder.onBind(position, String.valueOf(position));
//       }
//   };
//
//}
