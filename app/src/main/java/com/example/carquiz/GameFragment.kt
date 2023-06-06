package com.example.carquiz

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.carquiz.databinding.FragmentGameBinding
import kotlin.properties.Delegates



class GameFragment : Fragment() {
    data class Question(
        val image:Int,
        val options:List<String>,
        val answer:Int
    )
    private lateinit var binding: FragmentGameBinding
    private val questions:MutableList<Question>
            =mutableListOf(
        Question(R.drawable.bently,options= listOf("Bentley","Rolls Royce","Range Rover","Honda"),1),
        Question(R.drawable.rollsroyce,options= listOf("Bentley","Rolls Royce","Range Rover","Honda"),2),
        Question(R.drawable.rangerover,options= listOf("Bentley","Rolls Royce","Range Rover","Honda"),3),
        Question(R.drawable.lambor,options= listOf("Bentley","Lamborghini","Range Rover","Honda"),2),
        Question(R.drawable.ferrari,options= listOf("Bentley","Rolls Royce","Range Rover","Ferrari"),4),
        Question(R.drawable.f1,options= listOf("f1","Rolls Royce","Range Rover","Honda"),1),
        Question(R.drawable.supra,options= listOf("Bentley","Supra","Range Rover","Honda"),2),
        Question(R.drawable.mustang,options= listOf("Mustang","Rolls Royce","Range Rover","Honda"),1),
    )
    private lateinit var CurrentQuestion:Question
    private lateinit var options:MutableList<String>
    private var answer=0
    private var questionIndex=0
    private var score=0
    private var runner=1
    private var check=0
    private var numQuestions=8
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
       binding=FragmentGameBinding.inflate(layoutInflater)
        randomizeQuestion()
        if(savedInstanceState!=null){
            score=savedInstanceState.getInt(score.toString())
            runner=savedInstanceState.getInt(runner.toString())
            answer=savedInstanceState.getInt(answer.toString())
        }
        binding.submit.setOnClickListener{
            view:View->
            check=binding.radioGroup.checkedRadioButtonId
            check=check%2131230825
            /*if(-1!=check)
            {
                val answerId=answer
                when(check)
                {
                    answerId-> {
                        updateScore()
                    }
                    else-> {
                        score += 0
                    }
                }
            }*/
            if(check+1==answer)
            {
                updateScore()
                Toast.makeText(this.context,"Correct answer",Toast.LENGTH_SHORT).show()

            }
            else
            {
                Toast.makeText(this.context,"Wrong answer",Toast.LENGTH_SHORT).show()
            }
            if(runner<numQuestions){
                CurrentQuestion=questions[questionIndex]
                binding.score.text= score.toString()
                setQuestion()
                binding.radioGroup.clearCheck()
                binding.invalidateAll()
            }
            else
            {

                val bundle = Bundle()
                bundle.putString("score", score.toString())

                val winnerFragment = WinnerFragment()
                winnerFragment.arguments = bundle

                view.findNavController().navigate(R.id.action_gameFragment_to_winnerFragment, bundle)


            }

        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("score_key",score)
        outState.putInt("runner_key",runner)
        outState.putInt("answer",answer)


    }

    private fun updateScore() {
        score++
    }

    private fun randomizeQuestion() {
        questions.shuffle()
        questionIndex=0
        setQuestion()
    }

    private fun setQuestion() {
        runner++
        questionIndex++
        CurrentQuestion=questions[questionIndex]
        options=CurrentQuestion.options.toMutableList()
        answer=CurrentQuestion.answer
        binding.img.setImageResource(CurrentQuestion.image)
        binding.btn1.text=options[0]
        binding.btn2.text=options[1]
        binding.btn3.text=options[2]
        binding.btn4.text=options[3]


    }
}