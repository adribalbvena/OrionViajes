package ar.edu.ort.orionviajes.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ar.edu.ort.orionviajes.Constants
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.api.ApiClient
import ar.edu.ort.orionviajes.data.Expense
import ar.edu.ort.orionviajes.data.ExpensesResponse
import ar.edu.ort.orionviajes.databinding.FragmentDashboardBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart
    private var expensesForCategory = ArrayList<Float>()
    private var expensesForPaymentMethod = ArrayList<Float>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val travel = DashboardFragmentArgs.fromBundle(requireArguments()).travel
        getExpenses(travel.id)

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
            val value = expensesForCategory[i]
            val label = Constants.CATEGORIES[i]
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
        colors.add(resources.getColor(R.color.light_yellow))
        colors.add(resources.getColor(R.color.harvest_gold))
        colors.add(resources.getColor(R.color.brown))
        colors.add(resources.getColor(R.color.teal_tree))

        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(object: ValueFormatter(){
            override fun getFormattedValue(value: Float): String {
                val df = DecimalFormat("0 %")
                df.roundingMode = RoundingMode.CEILING
                return df.format(value/100)
            }
        })

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

    fun setBarChart(){
        barChart = binding.barChartPaymentMethod

        //carga de datos
        var barEntries: ArrayList<BarEntry> = ArrayList()
        var i = 0
        for (expense in expensesForPaymentMethod) {
            barEntries.add(BarEntry(i.toFloat(), expense))
            i++
        }

        val barDataSet = BarDataSet(barEntries, "")

        var labels = Constants.PAYMENT_METHOD.toTypedArray()

        val data = BarData(barDataSet)
        data.setValueFormatter(object: ValueFormatter(){
            override fun getFormattedValue(value: Float): String {
                val df = DecimalFormat("$ #,###.##");
                df.roundingMode = RoundingMode.FLOOR
                return df.format(value)
            }
        })

        barChart.data = data

        barChart.legend.isEnabled = false
        barChart.description.isEnabled = false
        barChart.setDrawValueAboveBar(false)
        barChart.data.setValueTextSize(12f)

        //Valores X configuraciones
        val xaxis = barChart.xAxis
        xaxis.position = XAxis.XAxisPosition.BOTTOM
        xaxis.isGranularityEnabled = true
        xaxis.granularity = 1f

        //esto sirve para poder poner las etiquetas con nombres
        val formatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return labels.get(value.toInt())
            }
        }

        xaxis.valueFormatter = formatter

        //xaxis.setDrawLabels(false)

        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.orange_expense))
        colors.add(resources.getColor(R.color.ocean_depths))

        barDataSet.colors = colors


        barChart.animateY(1400)

    }

    private fun getExpenses(travel_id : String){
        val apiService = ApiClient.getTravelsApi(requireContext())
        val call = apiService.getExpenses(travel_id)
        call.enqueue(object : Callback<ExpensesResponse>{
            override fun onResponse(
                call: Call<ExpensesResponse>,
                response: Response<ExpensesResponse>
            ) {
                if (response.isSuccessful){
                    prepareDataForCharts(response.body()!!)
                }
            }

            override fun onFailure(call: Call<ExpensesResponse>, t: Throwable) {
                Snackbar.make(requireView(), "Ups! Algo salió mal", Snackbar.LENGTH_LONG).show()
            }

        })


    }

    private fun prepareDataForCharts(list : ArrayList<Expense>){
        var countFood = 0f
        var countDrinks = 0f
        var countTransport = 0f
        var countAccommodation = 0f
        var countEntertainment = 0f
        var countOthers = 0f
        var countCard = 0f
        var countCash = 0f
        for (expense in list) {
            when(expense.category) {
                "Comida" -> countFood+= expense.amount
                "Bebida" -> countDrinks+= expense.amount
                "Transporte" -> countTransport+= expense.amount
                "Alojamiento" -> countAccommodation+= expense.amount
                "Entretenimiento" -> countEntertainment+= expense.amount
                "Otros" -> countOthers+= expense.amount
            }
            when(expense.paymentMethod) {
                "Tarjeta" -> countCard+= expense.amount
                "Efectivo" -> countCash+= expense.amount
            }

        }
        val total = countFood + countDrinks + countTransport + countAccommodation + countEntertainment + countOthers
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

        expensesForPaymentMethod.add(countCard)
        expensesForPaymentMethod.add(countCash)

        //Seteamos el grafico
        setPieChart()
        setBarChart()
    }


}