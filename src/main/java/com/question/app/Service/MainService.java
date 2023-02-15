package com.question.app.Service;

import com.question.app.graph.AbstractGraph;
import com.question.app.graph.Displayable;
import com.question.app.graph.Graph;
import com.question.app.graph.WeightedGraph;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

@SuppressWarnings("unused")
public class MainService extends Application {
    private final TextField tfVertexName = new TextField();
    private final TextField tfX = new TextField();
    private final TextField tfY = new TextField();
    private final Button btAddVertex = new Button("确认添加");

    private final TextField tfu = new TextField();
    private final TextField tfv = new TextField();
    private final TextField tfWeight = new TextField();
    private final Button btAddEdge = new Button("确认添加");

    private final TextField tfStartVertex = new TextField();
    private final TextField tfEndVertex = new TextField();
    private final TextField tfPassbyVertex = new TextField();
    private final Button btFindShortestPath = new Button("计算最短路径");

    private final Button btStartOver = new Button("全部删除");
    private final Label lblStatus = new Label();

    private final WeightedGraph<Vertex> graph = new WeightedGraph<>();
    private final GraphView view = new GraphView(graph);

    public Map<String, Integer> vertices = new HashMap<>();
    private int count = 0;

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        GridPane gridPane1 = new GridPane();
        gridPane1.add(new Label("添加新的景点"), 0, 0);
        gridPane1.add(new Label("景点名称:"), 0, 1);
        gridPane1.add(new Label("X坐标:"), 0, 2);
        gridPane1.add(new Label("Y坐标:"), 0, 3);
        gridPane1.add(tfVertexName, 1, 1);
        gridPane1.add(tfX, 1, 2);
        gridPane1.add(tfY, 1, 3);
        gridPane1.add(btAddVertex, 1, 4);

        GridPane gridPane2 = new GridPane();
        gridPane2.add(new Label("添加新的路径"), 0, 0);
        gridPane2.add(new Label("景点1:"), 0, 1);
        gridPane2.add(new Label("景点2:"), 0, 2);
        gridPane2.add(new Label("路径长度:"), 0, 3);
        gridPane2.add(tfu, 1, 1);
        gridPane2.add(tfv, 1, 2);
        gridPane2.add(tfWeight, 1, 3);
        gridPane2.add(btAddEdge, 1, 4);

        GridPane gridPane3 = new GridPane();
        gridPane3.add(new Label("查找最短路径"), 0, 0);
        gridPane3.add(new Label("起点:"), 0, 1);
        gridPane3.add(new Label("途径:"), 0, 2);
        gridPane3.add(new Label("终点:"), 0, 3);
        gridPane3.add(tfStartVertex, 1, 1);
        gridPane3.add(tfPassbyVertex, 1, 2);
        gridPane3.add(tfEndVertex, 1, 3);
        gridPane3.add(btFindShortestPath, 1, 4);

        HBox hBox = new HBox(5);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(gridPane1, gridPane2, gridPane3);

        VBox vBox = new VBox(5);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(hBox, btStartOver);
        BorderPane pane = new BorderPane();
        pane.setCenter(view);
        pane.setBottom(vBox);
        BorderPane.setAlignment(lblStatus, Pos.CENTER);

        // Create a scene and place it in the stage
        Scene scene = new Scene(pane, 850, 350);
        primaryStage.setTitle("景点最短路径"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        // 添加景点结点
        btAddVertex.setOnAction(e -> {
            try {
                String display = tfVertexName.getText();
                int x = Integer.parseInt(tfX.getText().trim()); // x坐标
                int y = Integer.parseInt(tfY.getText().trim()); // y坐标

                this.addVertexAndIndex(display);
                graph.addVertex(new Vertex(display, x, y)); // 确认添加
                view.paint();
            }
            catch (Exception ex) {
                lblStatus.setText("只能输入整型数字");
            }
        });

        // 添加边
        btAddEdge.setOnAction(e -> {
            try {
                String us = tfu.getText();
                String vs = tfv.getText();
                int weight = Integer.parseInt(tfWeight.getText().trim());
                int u = this.vertices.get(us);
                int v = this.vertices.get(vs);

                if (us.equals(vs))
                    lblStatus.setText("两个景点不能相同");
                else {
                    graph.addEdge(u, v, weight);
                    graph.addEdge(v, u, weight);
                    view.paint();
                }
            } catch (Exception ex) {
                lblStatus.setText("只能输入整型数字");
            }
        });

        // 查找最短路径
        btFindShortestPath.setOnAction(e -> {
            try {
                String us = tfStartVertex.getText().trim();
                String vs = tfEndVertex.getText().trim();
                String ps = tfPassbyVertex.getText().trim();

                String[] passedVertices;
                if (ps.isEmpty()) {
                    passedVertices = (us + " " + vs).split(" ");
                } else {
                    passedVertices = (us + " " + ps + " " + vs).split(" ");
                }
                List<Vertex> path = new LinkedList<>();

                // 分段Dijkstra
                for (int i = 0; i <= passedVertices.length-2; i++) {
                    int p = this.vertices.get(passedVertices[i]); // 经过点索引
                    int n = this.vertices.get(passedVertices[i+1]); // 下一个经过点索引
                    AbstractGraph<Vertex>.Tree tempTree = graph.getShortestPath(p); // 从p出发

                    System.out.println(i + " => " + p + passedVertices[i] + " - " + n + passedVertices[i+1]); // 到达n的最短路径

                    List<Vertex> tempPath = tempTree.getPath(n);
                    Collections.reverse(tempPath);
                    path.addAll(tempPath);
                    for (Vertex vertex : path) {
                        System.out.print(vertex.display + " -> ");
                    }
                    System.out.println();

                    if (i != passedVertices.length - 2) {
                        path.remove(path.size()-1); // 如果不是最后一个就移除path的最后一个点
                    }
                }

                view.setPath(path);
                view.paint();
            } catch (Exception ex) {
                lblStatus.setText("只能输入整型数字");
            }
        });

        // 全部清除
        btStartOver.setOnAction(e -> {
            graph.clear();
            vertices.clear();
            view.setPath(null);
            view.paint();
            count = 0;
        });
    }

    private void addVertexAndIndex(String name) {
        Integer old = vertices.putIfAbsent(name, count);
        int current = old == null ? count++ : old;
        // old != null 已经有了就直接返回 old
        // old == null 新建的就返回count并向下偏移
    }

    static class Vertex implements Displayable {
        private final int x, y;
        private final String display;

        Vertex(String display, int x, int y) {
            this.display = display;
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }


        public String getDisplay() {
            return display;
        }

        @Override
        @SuppressWarnings("all")
        public boolean equals(Object o) {
            return this.display.equals(((Vertex)o).display);
        }
    }

    static class GraphView extends Pane {
        private final Graph<? extends Displayable> graph;
        private List<? extends Displayable> path;

        public GraphView(Graph<? extends Displayable> graph,
                         List<? extends Displayable> path) {
            this.graph = graph;
            this.path = path;
        }

        public GraphView(Graph<? extends Displayable> graph) {
            this.graph = graph;
        }

        public void setPath(List<? extends Displayable> path) {
            this.path = path;
            paint();
        }

        @SuppressWarnings("all")
        protected void paint() {
            getChildren().clear();

            // Draw vertices
            List<? extends Displayable> vertices = graph.getVertices();

            for (int i = 0; i < graph.getSize(); i++) {
                int x = vertices.get(i).getX();
                int y = vertices.get(i).getY();
                String name = vertices.get(i).getDisplay();

                getChildren().addAll(new Circle(x, y, 8),
                        new Text(x - 12, y - 12, name));
            }

            // Display edges and weights
            for (int i = 0; i < graph.getSize(); i++) {
                List<Integer> neighbors = graph.getNeighbors(i);
                for (int v : neighbors) {
                    int x1 = graph.getVertex(i).getX();
                    int y1 = graph.getVertex(i).getY();
                    int x2 = graph.getVertex(v).getX();
                    int y2 = graph.getVertex(v).getY();

                    try {
                        getChildren().addAll(new Line(x1, y1, x2, y2),
                                new Text((x1 + x2) / 2 - 4, (y1 + y2) / 2 - 6,
                                        ((WeightedGraph) graph).getWeight(i, v) + ""));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            // Display the path
            if (path == null) return;
            for (int i = 1; i < path.size(); i++) {
                int x1 = path.get(i).getX();
                int y1 = path.get(i).getY();
                int x2 = path.get(i - 1).getX();
                int y2 = path.get(i - 1).getY();
                Line line = new Line(x1, y1, x2, y2);
                line.setStroke(Color.RED);
                line.setStrokeWidth(3);
                this.getChildren().add(line);
            }
        }
    }

    /**
     * The main method is only needed for the IDE with limited
     * JavaFX support. Not needed for running from the command line.
     */
    public static void run(String[] args) {
        launch(args);
    }
}

