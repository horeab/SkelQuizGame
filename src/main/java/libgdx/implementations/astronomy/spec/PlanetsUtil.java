package libgdx.implementations.astronomy.spec;

import com.google.gson.Gson;
import libgdx.campaign.QuestionConfigFileHandler;
import libgdx.implementations.astronomy.AstronomyCategoryEnum;
import libgdx.implementations.astronomy.AstronomyDifficultyLevel;
import libgdx.implementations.screens.implementations.astronomy.AstronomyPlanetsGameType;
import libgdx.implementations.skelgame.question.GameQuestionInfoStatus;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class PlanetsUtil {

    public static List<Planet> getAllPlanets() {
        List<Planet> planets = Arrays.asList(new Gson().fromJson(getAllFileText(), Planet[].class));
        planets.get(10).setName(SpecificPropertiesUtils.getText("astronomy_moon"));
        String file = new QuestionConfigFileHandler().getFileText(AstronomyDifficultyLevel._0, AstronomyCategoryEnum.cat0);
        Scanner scanner = new Scanner(file);
        int i = 0;
        while (scanner.hasNextLine()) {
            String[] info = scanner.nextLine().split(":");
            planets.get(i).setName(info[2]);
            i++;
        }
        return planets;
    }


    public static Map<Integer, GameQuestionInfoStatus> getAllAvailableLevelsToPlay(List<Planet> allPlanets, AstronomyPlanetsGameType planetsGameType) {
        Map<Integer, GameQuestionInfoStatus> res = new LinkedHashMap<>();
        for (Planet planet : allPlanets) {
            if (PlanetsUtil.isValidOption(planet.getId(), planetsGameType, allPlanets)) {
                res.put(planet.getId(), GameQuestionInfoStatus.OPEN);
            }
        }
        return res;
    }

    public static String getName(int id, List<Planet> planets) {
        for (Planet e : planets) {
            if (e.getId() == id) {
                return e.getName();
            }
        }
        return null;
    }

    public static boolean isValidOption(int id, AstronomyPlanetsGameType planetsGameType, List<Planet> planets) {
        if (planetsGameType == AstronomyPlanetsGameType.SUN_LIGHT_DURATION) {
            return getById(id, planets).getLightFromSunInSec() > 0;
        } else if (planetsGameType == AstronomyPlanetsGameType.MASS) {
            return true;
        } else if (planetsGameType == AstronomyPlanetsGameType.GRAVITY) {
            return true;
        } else if (planetsGameType == AstronomyPlanetsGameType.MEAN_TEMPERATURE) {
            return true;
        } else if (planetsGameType == AstronomyPlanetsGameType.ORBITAL_PERIOD) {
            return getById(id, planets).getOrbitalPeriodInDays() > 0;
        } else if (planetsGameType == AstronomyPlanetsGameType.RADIUS) {
            return true;
        }
        return false;
    }

    public static String getLightFromSunInSec(int id, List<Planet> planets) {
        for (Planet e : planets) {
            if (e.getId() == id) {
                return formatSeconds(e.getLightFromSunInSec());
            }
        }
        return null;
    }

    public static String formatSeconds(int timeInSeconds) {
        int hours = timeInSeconds / 3600;
        int secondsLeft = timeInSeconds - hours * 3600;
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String formattedTime = "";
        if (hours > 0) {
            formattedTime += hours + "h ";
        }

        if (minutes > 0) {
            formattedTime += minutes + "min ";
        }

        if (seconds > 0 && !formattedTime.contains("h")) {
            formattedTime += seconds + "s";
        }

        return formattedTime;
    }

    public static String getGravityInRelationToEarth(int id, List<Planet> planets) {
        for (Planet e : planets) {
            if (e.getId() == id) {
                float gravityInRelationToEarth = e.getGravityInRelationToEarth();
                DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat df = new DecimalFormat("#.#", otherSymbols);
                String res = df.format(gravityInRelationToEarth) + " kg";
                if (gravityInRelationToEarth < 1) {
                    res = df.format(gravityInRelationToEarth * 1000) + " g";
                }
                return res;
            }
        }
        return null;
    }

    public static String getOrbitalPeriod(int id, List<Planet> planets) {
        for (Planet e : planets) {
            if (e.getId() == id) {
                int orbitalPeriodInDays = e.getOrbitalPeriodInDays();
                String res = orbitalPeriodInDays + " days";
                if (orbitalPeriodInDays > 999) {

                    res = String.format("%.0f", (orbitalPeriodInDays / 365f)) + " years";
                }
                return res;
            }
        }
        return null;
    }

    public static String getRadius(int id, List<Planet> planets) {
        for (Planet e : planets) {
            if (e.getId() == id) {
                float earthRadius = getById(3, planets).getRadius();
                return formatDecimalValue(e.getRadius() / earthRadius);
            }
        }
        return null;
    }

    public static Planet getById(int id, List<Planet> planets) {
        for (Planet e : planets) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    public static String getMeanTemp(int id, List<Planet> planets) {
        for (Planet e : planets) {
            if (e.getId() == id) {
                return e.getMeanTempInC() + " °C";
            }
        }
        return null;
    }

    public static String getMassInRelationToEarth(int id, List<Planet> planets) {
        for (Planet e : planets) {
            if (e.getId() == id) {
                float massInRelationToEarth = e.getMassInRelationToEarth();
                return formatDecimalValue(massInRelationToEarth);
            }
        }
        return null;
    }

    private static String formatDecimalValue(float massInRelationToEarth) {
        String res;
        if (massInRelationToEarth >= 1) {
            res = ((int) Math.round(massInRelationToEarth)) + "";
        } else {
            String dedcimalPlaces = "%.01f";
            if (massInRelationToEarth < 0.009f) {
                dedcimalPlaces = "%.03f";
            } else if (massInRelationToEarth < 0.09f) {
                dedcimalPlaces = "%.02f";
            }
            res = String.format(dedcimalPlaces, massInRelationToEarth);
        }
        return res + " x ";
    }


    public static String getAllFileText() {
        return new QuestionConfigFileHandler().getFileText("questions/planets.json");
    }

}
