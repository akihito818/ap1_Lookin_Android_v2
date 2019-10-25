package com.example.lookin

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*
import com.example.lookin.R


class ARActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var arrayView : Array<View>

    lateinit var pin2Renderable: ModelRenderable
    lateinit var pinRenderable: ModelRenderable
    lateinit var arFragment: ArFragment

    internal  var selected = 1

    override fun onClick(view: View?){
        if(view!!.id== R.id.pin2){
            selected=1
            mysetBackground(view!!.id)
        }

    }

    private fun mysetBackground(id: Int) {
        for(i in arrayView.indices){
            if(arrayView[i].id==id)
                arrayView[i].setBackgroundColor(Color.parseColor("#80333639"))
            else
                arrayView[i].setBackgroundColor(Color.TRANSPARENT)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        setupArray()
        setupClickListener()
        setupModel()


        arFragment = supportFragmentManager.findFragmentById(R.id.scene_form_fragment)as ArFragment
        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)

            createModel(anchorNode,selected)



        }


    }



    private  fun createModel(anchorNode: AnchorNode,selected: Int) {

        if(selected==1){
            val pin2 = TransformableNode(arFragment.transformationSystem)
            pin2.setParent(anchorNode)
            pin2.renderable = pin2Renderable
            pin2.select()

            addName(anchorNode,pin2,"pin2");


        }

    }

    private fun addName(anchorNode: AnchorNode, node: TransformableNode, name: String) {
        ViewRenderable.builder().setView(this,R.layout.content_main)
            .build()
            .thenAccept { ViewRenderable ->
                val nameView = TransformableNode(arFragment.transformationSystem)
                nameView.localPosition = Vector3(0f,node.localPosition.y+0.5f,0f)
                nameView.setParent(anchorNode)
                nameView.renderable = ViewRenderable
                nameView.select()

                val txt_name =ViewRenderable.view as TextView
                val intent = Intent(this, MainActivity::class.java)
                txt_name.text=name
                txt_name.setOnClickListener{
                    anchorNode.setParent(null)
                    startActivity(intent)
                }

            }




    }


    private fun setupModel() {
        ModelRenderable.builder().
            setSource(this,R.raw.pin2)
            .build()
            .thenAccept{ modelRenderable -> pin2Renderable = modelRenderable  }
            .exceptionally{ throwable ->
                Toast.makeText(this,"Unnable to load pin2 model",Toast.LENGTH_SHORT).show()
                null
            }



    }


    private fun setupClickListener() {
        for(i in arrayView.indices ){
            arrayView[i].setOnClickListener(this)
        }
    }

    private fun setupArray(){
        arrayView=arrayOf(
            pin2)
    }

}

