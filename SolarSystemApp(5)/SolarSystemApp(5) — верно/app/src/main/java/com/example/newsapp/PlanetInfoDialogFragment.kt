import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.newsapp.R

class PlanetInfoDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Создаем Builder для диалога
        val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_info, null)

        // Получаем данные из аргументов
        val planetName = arguments?.getString("planet_name")
        val planetImageResId = arguments?.getInt("planet_image")

        // Получаем ссылки на элементы интерфейса
        val imageView: ImageView = view.findViewById(R.id.planet_image)
        val textView: TextView = view.findViewById(R.id.planet_info)
        val closeButton: Button = view.findViewById(R.id.close_button)

        // Устанавливаем изображение и информацию
        imageView.setImageResource(planetImageResId ?: 0)
        textView.text = getString(R.string.planet_info, planetName)

        // Устанавливаем слушатель для кнопки закрытия
        closeButton.setOnClickListener {
            dismiss()
        }

        // Устанавливаем вид для диалога
        builder.setView(view)

        return builder.create()
    }

    companion object {
        fun newInstance(planetName: String, planetImageResId: Int): PlanetInfoDialogFragment {
            val fragment = PlanetInfoDialogFragment()
            val args = Bundle()
            args.putString("planet_name", planetName)
            args.putInt("planet_image", planetImageResId)
            fragment.arguments = args
            return fragment
        }
    }
}
