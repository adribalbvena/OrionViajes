package ar.edu.ort.orionviajes.fragments

import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ar.edu.ort.orionviajes.Constants
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.data.Expense
import ar.edu.ort.orionviajes.data.Travel
import ar.edu.ort.orionviajes.databinding.FragmentDashboardBinding
import ar.edu.ort.orionviajes.factories.ExpenseViewModelFactory
import ar.edu.ort.orionviajes.factories.ExpensesListViewModelFactory
import ar.edu.ort.orionviajes.viewmodels.ExpenseViewModel
import ar.edu.ort.orionviajes.viewmodels.ExpensesListViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import kotlin.math.exp

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var pieChart: PieChart
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var expensesList: ArrayList<Expense>
    private var expensesForCategory = ArrayList<Float>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val travel = DashboardFragmentArgs.fromBundle(requireArguments()).travel

        initExpenseViewModel(travel)
        addObservable()


        return binding.root
    }

    fun setPieChart(){

        pieChart = binding.pieChartCategory

        pieChart.setUsePercentValues(true)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        pieChart.setDragDecelerationFrictionCoef(0.95f)

        pieChart.setDrawHoleEnabled(true)
        pieChart.setHoleColor(Color.WHITE)

        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        pieChart.setHoleRadius(58f)
        pieChart.setTransparentCircleRadius(61f)

        pieChart.setDrawCenterText(false)
        pieChart.setRotationAngle(5f)
        pieChart.setRotationEnabled(true)
        pieChart.setHighlightPerTapEnabled(true)


        pieChart.animateY(1400, Easing.EaseInOutQuad)

        pieChart.legend.isEnabled = true
        pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        pieChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        pieChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        pieChart.setDrawEntryLabels(false)



        //Carga de datos
        val entries: ArrayList<PieEntry> = ArrayList()
        var i = 0
        for (entry in expensesForCategory){
            var value = expensesForCategory[i]
            var label = Constants.CATEGORIES[i]
            entries.add(PieEntry(value, label))
            i++
        }

        val dataSet = PieDataSet(entries, "")

        dataSet.setDrawIcons(false)

        dataSet.sliceSpace = 2f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 8f


        //Agregando colores
        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.orange_expense))
        colors.add(resources.getColor(R.color.ocean_depths))
        colors.add(resources.getColor(R.color.beige))
        colors.add(resources.getColor(R.color.harvest_gold))
        colors.add(resources.getColor(R.color.brown))
        colors.add(resources.getColor(R.color.teal_tree))

        dataSet.colors = colors

        //Dataset on below line we are setting pie data set
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(14f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        pieChart.setData(data)
        pieChart.description.text = ""


        // Deshacer highlights
        pieChart.highlightValues(null)

        //Cargando el grafico
        pieChart.invalidate()


    }

    fun initExpenseViewModel(travel: Travel) {
        activity?.let {
            expenseViewModel = ViewModelProvider(
                this,
                ExpenseViewModelFactory(it)
            ).get(ExpenseViewModel::class.java)
        }
        expenseViewModel.getExpenses(travel.id)

    }

    fun addObservable(){
        expenseViewModel.expenses.observe(viewLifecycleOwner, Observer {
            expensesList = it as ArrayList<Expense>
            var countFood = 0f
            var countDrinks = 0f
            var countTransport = 0f
            var countAccommodation = 0f
            var countEntertainment = 0f
            var countOthers = 0f
            for (expense in expensesList) {
                when(expense.category) {
                    "Comida" -> countFood+= expense.amount
                    "Bebida" -> countDrinks+= expense.amount
                    "Transporte" -> countTransport+= expense.amount
                    "Alojamiento" -> countAccommodation+= expense.amount
                    "Entretenimiento" -> countEntertainment+= expense.amount
                    "Otros" -> countOthers+= expense.amount
                }
            }
            var total = countFood + countDrinks + countTransport + countAccommodation + countEntertainment + countOthers
            countFood = (countFood / total) * 100
            countDrinks = (countDrinks / total) * 100
            countTransport = (countTransport / total) * 100
            countAccommodation = (countAccommodation / total) * 100
            countEntertainment = (countEntertainment / total) * 100
            countOthers = (countOthers / total) * 100

            expensesForCategory.add(countFood)
            expensesForCategory.add(countDrinks)
            expensesForCategory.add(countTransport)
            expensesForCategory.add(countAccommodation)
            expensesForCategory.add(countEntertainment)
            expensesForCategory.add(countOthers)

            //Seteamos el grafico
            setPieChart()
        })
    }

}