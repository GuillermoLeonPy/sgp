package py.com.kyron.sgp.gui.component;

import java.util.ArrayList;
import java.util.List;

import py.com.kyron.sgp.gui.DashboardUI;
import py.com.kyron.sgp.gui.data.dummy.DummyDataGenerator;
import py.com.kyron.sgp.gui.domain.Movie;

//import com.vaadin.addon.charts.Chart;
//import com.vaadin.addon.charts.model.ChartType;
//import com.vaadin.addon.charts.model.Credits;
//import com.vaadin.addon.charts.model.ListSeries;
//import com.vaadin.addon.charts.model.PlotOptionsBar;
//import com.vaadin.addon.charts.model.Series;

@SuppressWarnings("serial")
public class TopGrossingMoviesChart /*extends Chart*/ {
/*
    public TopGrossingMoviesChart() {
        setCaption("Top Grossing Movies");
        getConfiguration().setTitle("");
        getConfiguration().getChart().setType(ChartType.BAR);
        getConfiguration().getChart().setAnimation(false);
        getConfiguration().getxAxis().getLabels().setEnabled(false);
        getConfiguration().getxAxis().setTickWidth(0);
        getConfiguration().getyAxis().setTitle("");
        setSizeFull();

        List<Movie> movies = new ArrayList<Movie>(DashboardUI.getDataProvider()
                .getMovies());

        List<Series> series = new ArrayList<Series>();
        for (int i = 0; i < 6; i++) {
            Movie movie = movies.get(i);
            PlotOptionsBar opts = new PlotOptionsBar();
            opts.setColor(DummyDataGenerator.chartColors[5 - i]);
            opts.setBorderWidth(0);
            opts.setShadow(false);
            opts.setPointPadding(0.4);
            opts.setAnimation(false);
            ListSeries item = new ListSeries(movie.getTitle(), movie.getScore());
            item.setPlotOptions(opts);
            series.add(item);

        }
        getConfiguration().setSeries(series);

        Credits c = new Credits("");
        getConfiguration().setCredits(c);

        PlotOptionsBar opts = new PlotOptionsBar();
        opts.setGroupPadding(0);
        getConfiguration().setPlotOptions(opts);

    }*/
}
