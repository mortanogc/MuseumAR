package com.example.museumar.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.museumar.R;
import com.example.museumar.model.News;

import java.util.ArrayList;
import java.util.List;

public class NewsListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        NewsListAdapter adapter = new NewsListAdapter(createSampleNews(), getContext());
        recyclerView.setAdapter(adapter);

        // Добавьте разделитель между элементами списка
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), R.drawable.divider_news);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private List<News> createSampleNews() {
        List<News> newsList = new ArrayList<>();
        newsList.add(new News("Новые технологии уже здесь", "В нашем приложении с новым обновленем было предвнесено нечто новое, что мы ранее никогда с вами не видели среди всех музеев нашей области, а именно технологии дополненной реальности! Пока такая функция только в тестировочном режиме, но скоро с обновлениями мы сможем оптимизировать и добавить еще больше экспонатов для наших посетителей и желающих познать новое. Попробуйте новые технологии уже сегодня!", "https://media.proglib.io/posts/2020/04/01/c47ba74ff10a4c102bc171a255e8f1cc.png", "9.05.2023"));
        newsList.add(new News("Открытие выставки мундштуков и трубок", "Курительные аксессуары из янтаря и морской пенки XIX – начала XX века представлены на новой выставке «Сквозь дымку времени» в Музее янтаря. В экспозиции представлены трубки и мундштуки из коллекций петербургских художников Александра Журавлева и Александра Крылова. Они изготовлены из морской пенки (другое название минерала сепиолита).\n" +
                "\n" +
                "Как рассказала на церемонии открытия дочь Александра Журавлева Анастасия Никифорова, большая часть экспонатов покупалась ее отцом на барахолках и в маленьких магазинчиках старинных вещей. Многие трубки требовали реставрации и замены утраченных деталей. Украшением коллекции стал восточный кальян, который художник обнаружил в разобранном виде в одной из антикварных лавок.", "https://www.ambermuseum.ru/node_model_img?w=0&h=0&mode=&title=&file=bdc5ae0b3f6a437e2847dbc6c6119e05.jpg", "2.05.2023"));
        newsList.add(new News("Обратная связь и новый этап улучшений", "С сегодняшнего дня и нового обновления, версии 1.1.1, в приложении появились отзывы о нашем музейном комплексе, которые вы, наши любимые посетители можете оставить и оценить качество и эмоции от посещения и так же прочитать и посмотреть отзывы других людей.", "https://tvoi-setevichok.ru/wp-content/uploads/2019/08/02087486.jpg", "01.05.2023"));
        newsList.add(new News("Акция «Музейная ночь» пройдет 19 мая", "Содержание19 мая Музей янтаря будет открыт для посетителей до поздней ночи и представит специальную программу «Ключ от города». Она предложит взглянуть на музей и в целом на город с необычного ракурса.\n" +
                "Программа начнется в 20:00 и закончится в полночь.\n" +
                "\n" +
                "Перед воротами музея стартует ночной городской велоквест – «Межмузейная гонка – 6». Маршрут объединит калининградские башни и башенки – самые известные и нетуристические, фортификационные и жилые. Для участия необходим смартфон с выходом в интернет и, конечно, велосипед. В подарок за правильно пройденный маршрут участники велоприключения получат призы. Присоединяйтесь!\n" +
                "\n" +
                "Основная программа Музейной ночи будет посвящена зданию Музея янтаря – башне «Дона», архитектурному и историческому памятнику.", "https://thumb.tildacdn.com/tild3363-6634-4261-b862-346537623238/-/format/webp/_N9B6619_-___.JPG", "27.04.2023"));
        newsList.add(new News("Новости уже здесь", "С новым обновлением приложения в нем появилась новая функция и новый главный экран - новости нашего музея, теперь вы всегда будете одними из первых, кто узнает про наши новые экспонаты и выставки, обеовления в работе или какие-то интересные новшества и происшествия в нашем музее", "https://img1.akspic.ru/attachments/originals/8/7/0/0/30078-ruka-karmannye_kompyutery-vosstanovlenie_dannyh-nogot-prisposoblenie-5183x3340.jpg", "25.04.2023"));
        //newsList.add(new News("Заголовок", "Содержание", "Image", "Date"));


        return newsList;
    }
}
