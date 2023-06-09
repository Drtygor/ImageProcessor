package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import model.filters.IFilter;
import model.filters.Normal;


/**
 * Represents the model for this program.
 */
public class ImageProcessorModel implements IImageProcessorModel {
  private int height;
  private int width;
  private int maxValue;
  private HashMap<String, ILayer> nameLayers;
  private List<ILayer> orderLayers;


  /**
   * Represents constructor for this model.
   * (load existing project)
   *
   * @param height      int height.
   * @param width       int width.
   * @param nameLayers  HashMap of layers with keys as names.
   * @param orderLayers List of layers.
   */
  public ImageProcessorModel(int height, int width, HashMap<String, ILayer> nameLayers,
                             List<ILayer> orderLayers, int maxValue) {
    if (height < 0 || width < 0 || nameLayers == null || orderLayers == null) {
      throw new IllegalArgumentException("invalid arguments");
    }
    this.height = height;
    this.width = width;
    this.maxValue = maxValue;
    this.nameLayers = nameLayers;
    this.orderLayers = orderLayers;
  }

  /**
   * Represents constructor for this model.
   * (load existing project)
   *
   * @param height      int height.
   * @param width       int width.
   * @param nameLayers  HashMap of layers with keys as names.
   * @param orderLayers List of layers.
   */
  public ImageProcessorModel(int height, int width, HashMap<String, ILayer> nameLayers,
                             List<ILayer> orderLayers) {
    this(height, width, nameLayers, orderLayers, 255);
  }

  /**
   * Represents a constructor for this model.
   * (new project)
   *
   * @param height int height.
   * @param width  int width.
   */
  public ImageProcessorModel(int height, int width) {
    if (height < 0 || width < 0) {
      throw new IllegalArgumentException("invalid arguments");
    }
    this.height = height;
    this.width = width;
    this.maxValue = maxValue;
    this.nameLayers = new HashMap<String, ILayer>();
    this.orderLayers = new ArrayList<ILayer>();
    this.addLayer("background", new Normal());
  }

  /**
   * This method gets the height of the image.
   *
   * @return int height.
   */
  public int getHeight() {
    return this.height;
  }

  /**
   * This method gets the width of the image.
   *
   * @return int width.
   */
  public int getWidth() {
    return this.width;
  }

  /**
   * This method gets the maxValue of the image.
   *
   * @return int max value.
   */
  @Override
  public int getMaxValue() {
    return this.maxValue;
  }

  /**
   * This method gets the layer from the project using a String key.
   *
   * @param string name of layer.
   * @return ILayer
   * @throws IllegalArgumentException if the layer does not exist.
   */
  @Override
  public ILayer getLayer(String string) throws IllegalArgumentException {
    if (!this.nameLayers.containsKey(string)) {
      throw new IllegalArgumentException("The layer does not exist.");
    }
    return this.nameLayers.get(string);
  }

  /**
   * This method gets the numbered layer from the image using an int key.
   *
   * @param num int layer number.
   * @return ILayer from image.
   */
  @Override
  public ILayer getLayer(int num) throws IllegalArgumentException {
    if (num < 0 || num >= this.orderLayers.size()) {
      throw new IllegalArgumentException("The layer does not exist.");
    }
    return this.orderLayers.get(num);
  }

  /**
   * This method will return all the layers from the project.
   *
   * @return list of Layers.
   */
  @Override
  public List<ILayer> getLayers() {
    return this.orderLayers;
  }

  /**
   * This method will return a map of all layers in the project.
   *
   * @return list of Layers.
   */
  @Override
  public HashMap<String, ILayer> getMapLayers() {
    return this.nameLayers;
  }

  /**
   * This method will return the position of a layer in the project given both their layer position.
   *
   * @param i layer position.
   * @param j layer position.
   */
  @Override
  public void swapLayers(int i, int j) {
    Collections.swap(this.orderLayers, i, j);
  }

  /**
   * This method will return the position of a layer in the project given their names.
   *
   * @param a name of layer.
   * @param b name of layer.
   * @throws IllegalArgumentException if the layers do not exist or if they are already in position.
   */
  @Override
  public void swapLayers(String a, String b) throws IllegalArgumentException {
    if (!this.nameLayers.containsKey(a) || !this.nameLayers.containsKey(b)) {
      throw new IllegalArgumentException("One of the layers does not exist.");
    }
    //FIXME: check if this is correct.
    if (this.getLayerPosition(this.getLayer(a).getName())
            == this.getLayerPosition(this.getLayer(b).getName())) {
      throw new IllegalArgumentException("The layers are already in the same position.");
    }

    Collections.swap(this.orderLayers, this.orderLayers.indexOf(this.getLayer(a)),
            this.orderLayers.indexOf(this.getLayer(b)));
  }


  /**
   * This method will return the position of a layer in the project given its name.
   *
   * @param string name of layer.
   * @return int position of layer.
   * @throws IllegalArgumentException if the layer does not exist.
   */
  public int getLayerPosition(String string) throws IllegalArgumentException {
    if (!this.nameLayers.containsKey(string)) {
      throw new IllegalArgumentException("The layer does not exist.");
    }

    return this.orderLayers.indexOf(this.getLayer(string));
  }

  /**
   * This method will return the position of a layer in the project given its layer.
   *
   * @param name   name of filter.
   * @param filter type of filter.
   * @throws IllegalArgumentException if the layer already exists.
   */
  public void addLayer(String name, IFilter filter) throws IllegalArgumentException {
    if (this.nameLayers.containsKey(name)) {
      throw new IllegalArgumentException("The layer already exists.");
    }
    this.nameLayers.put(name, (new Layer(name, filter, this.height, this.width)));
    this.orderLayers.add(this.nameLayers.get(name));
  }

  /**
   * This method will return the position of a layer in the project given its layer.
   *
   * @param name name of filter.
   * @throws IllegalArgumentException if the layer already exists.
   */
  public void addLayer(String name) throws IllegalArgumentException {
    if (this.nameLayers.containsKey(name)) {
      throw new IllegalArgumentException("The layer already exists.");
    }
    this.nameLayers.put(name, (new Layer(name, new Normal(), this.height, this.width)));
    this.orderLayers.add(this.nameLayers.get(name));
  }


  /**
   * This method will be used to add a layer to the project, without a filter.
   * This would be the background canvas.
   */
  public void newProject(int height, int width, int maxValue) {
    this.height = height;
    this.width = width;
    this.maxValue = maxValue;
    this.nameLayers = new HashMap<String, ILayer>();
    this.orderLayers = new ArrayList<ILayer>();
    this.addLayer("background", new Normal());
  }

  /**
   * This method will set a filter to a layer.
   *
   * @param name name of layer.
   * @throws IllegalArgumentException if the layer does not exist.
   */
  public void setFilter(String name, IFilter filter) throws IllegalArgumentException {
    if (!this.nameLayers.containsKey(name)) {
      throw new IllegalArgumentException("The layer does not exist.");
    }
    this.getLayer(name).setFilter(filter);
  }

  /**
   * This method will add an Image to a Layer.
   *
   * @param x     int x position.
   * @param y     int y position.
   * @param image IImage image.
   * @param layer ILayer layer.
   */
  public void addImage(int x, int y, IImage image, ILayer layer) throws IllegalArgumentException {
    if (x > this.width || x < 0 || y > this.height || y < 0) {
      throw new IllegalArgumentException("invalid arguments");
    }
    layer.addImage(image, x, y);
  }


  /**
   * This image will produce the final canvas for all layers for PPM.
   *
   * @return IPixel[][] finalPixels.
   */
  public IPixel[][] saveCanvas() {
    IPixel[][] finalPixels = new IPixel[getHeight()][getWidth()];
    for (int x = 0; x < orderLayers.size(); x++) {
      ILayer layer = orderLayers.get(x);
      layer.setCanvas(layer.getFilter().apply(orderLayers, layer));
      for (int i = 0; i < getHeight(); i++) {
        for (int j = 0; j < getWidth(); j++) {
          finalPixels[i][j] = orderLayers.get(x).getPixel(i, j);
        }
      }
    }
    return finalPixels;
  }

  /**
   * This method will return the list of layers in the project.
   *
   * @return String list of layers.
   */
  public String listLayers() {
    return String.join(",", nameLayers.keySet());
  }

  /**
   * This method will return the list of filters in the project.
   *
   * @return String list of filters.
   */
  public int getLayerCount() {
    return orderLayers.size();
  }


  /**
   * This method will return the list of filters in the project.
   *
   * @return String list of filters.
   */
  public BufferedImage compressImage() {
    BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
    for (int i = 0; i < this.height; i++) {
      for (int j = 0; j < this.width; j++) {
        IPixel[][] finalPixels = this.saveCanvas();
        int r = finalPixels[i][j].getRed();
        int g = finalPixels[i][j].getGreen();
        int b = finalPixels[i][j].getBlue();
        int a = finalPixels[i][j].getAlpha();

        int argb = a << 24;
        argb |= r << 16;
        argb |= g << 8;
        argb |= b;

        image.setRGB(j, i, argb);
      }
    }
    return image;
  }
}
