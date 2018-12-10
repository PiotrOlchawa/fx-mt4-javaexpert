package org.mt4expert.javaexpert.finder;


import org.mt4expert.javaexpert.config.ExpertConfigurator;
import org.mt4expert.javaexpert.data.Candle;
import org.mt4expert.javaexpert.data.Resistance;
import org.mt4expert.javaexpert.data.SRType;
import org.mt4expert.javaexpert.data.Support;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SupportResistanceFinder {

    static final Logger LOGGER = LoggerFactory.getLogger(SupportResistanceFinder.class);

    private Support supports = new Support();
    private Resistance resistances = new Resistance();
    Map<Candle, String> candleVortexMap;

    public SupportResistanceFinder(Map<Candle, String> candleVortexMap) {
        this.candleVortexMap = candleVortexMap;
    }

    public Support getSupports() {
        return supports;
    }

    public Resistance getResistances() {
        return resistances;
    }

    public void findSupportsAndResistances() {
        findSupports();
        findResistances();
    }

    private void findResistances() {
        List<Candle> possibleSupportVortexesList = candleVortexMap.entrySet().stream()
                .filter(l -> l.getValue().equals(ExpertConfigurator.VORTEX_HIGH))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        addResistanceCandle(possibleSupportVortexesList);
    }

    private void addResistanceCandle(List<Candle> possibleSupportVortexesList) {
        for (Candle candle : possibleSupportVortexesList) {
            for (Candle checkedCandle : possibleSupportVortexesList) {
                checkDependencyBetweenResistanceCandles(candle, checkedCandle);
            }
        }
    }

    private void checkDependencyBetweenResistanceCandles(Candle candle, Candle checkedCandle) {
        if (candle != checkedCandle && candle.getDate().compareTo(checkedCandle.getDate()) == -1) {

            // ResistanceTypes : R-CU-CHU-B,R-CU-CHU-NB,R-CU-CHD-B,R-CU-CHD-NB, R-CD-CHU-B,R-CD-CHU-NB,R-CD-CHD-B,R-CD-CHD-NB

            //check candle if up candle R-CU
            if (candle.getClose() - candle.getOpen() >= 0) {
                // check checkedCandle if up CHU
                if (checkedCandle.getClose() - checkedCandle.getOpen() >= 0) {
                    // check if high checkedCandle is above high of candle B
                    if (checkedCandle.getHigh() > candle.getHigh() && checkedCandle.getClose() < candle.getClose()) {
                        candle.setSRType(SRType.R_CU_CHU_B);
                        resistances.getResistanceCandlesList().add(checkedCandle);
                    }
                    // check if high checkedCandle is below high and above close of candle  NB
                    if (checkedCandle.getHigh() < candle.getHigh() && checkedCandle.getHigh() >= candle.getClose()) {
                        candle.setSRType(SRType.R_CU_CHU_NB);
                        resistances.getResistanceCandlesList().add(candle);
                    }
                }
                // check checkedCandle if down CHD
                if (checkedCandle.getClose() - checkedCandle.getOpen() < 0) {
                    // check if high checkedCandle is above high of candle B
                    if (checkedCandle.getHigh() > candle.getHigh() && checkedCandle.getOpen() < candle.getClose()) {
                        candle.setSRType(SRType.R_CU_CHD_B);
                        resistances.getResistanceCandlesList().add(checkedCandle);
                    }
                    // check if high checkedCandle is below high and above close of candle  NB
                    if (checkedCandle.getHigh() < candle.getHigh() && checkedCandle.getHigh() >= candle.getClose()) {
                        candle.setSRType(SRType.R_CU_CHD_NB);
                        resistances.getResistanceCandlesList().add(candle);
                    }
                }
            }

            //check candle if down candle R-CD
            if (candle.getClose() - candle.getOpen() < 0) {
                // check checkedCandle if up CHU
                if (checkedCandle.getClose() - checkedCandle.getOpen() >= 0) {
                    // check if high checkedCandle is above high of candle B
                    if (checkedCandle.getHigh() > candle.getHigh() && checkedCandle.getClose() < candle.getOpen()){
                        candle.setSRType(SRType.R_CD_CHU_B);
                        resistances.getResistanceCandlesList().add(checkedCandle);
                    }
                    // check if high checkedCandle is below high of candle NB
                    if (checkedCandle.getHigh() < candle.getHigh() && checkedCandle.getHigh() >= candle.getOpen()){
                        candle.setSRType(SRType.R_CD_CHU_NB);
                        resistances.getResistanceCandlesList().add(candle);
                    }
                }
                // check checkedCandle if down CHD
                if (checkedCandle.getClose() - checkedCandle.getOpen() < 0) {
                    // check if high checkedCandle is above high of candle B
                    if (checkedCandle.getHigh() > candle.getHigh() && checkedCandle.getOpen() < candle.getOpen()){
                        candle.setSRType(SRType.R_CD_CHD_B);
                        resistances.getResistanceCandlesList().add(checkedCandle);
                    }
                    // check if high checkedCandle is below high of candle NB
                    if (checkedCandle.getHigh() < candle.getHigh() && checkedCandle.getHigh() >= candle.getOpen()){
                        candle.setSRType(SRType.R_CD_CHD_NB);
                        resistances.getResistanceCandlesList().add(candle);
                    }
                }
            }

        }
    }

    private void findSupports() {
        List<Candle> possibleSupportVortexesList = candleVortexMap.entrySet().stream()
                .filter(l -> l.getValue().equals(ExpertConfigurator.VORTEX_LOW))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        addSupportCandle(possibleSupportVortexesList);
    }

    private void addSupportCandle(List<Candle> possibleSupportVortexesList) {
        for (Candle candle : possibleSupportVortexesList) {
            for (Candle checkedCandle : possibleSupportVortexesList) {
                checkDependencyBetweenSupportCandles(candle, checkedCandle);
            }
        }
    }

    private void checkDependencyBetweenSupportCandles(Candle candle, Candle checkedCandle) {
        if (candle != checkedCandle && candle.getDate().compareTo(checkedCandle.getDate()) == -1) {

            // Support Types: S-CU-CHU-B,S-CU-CHU-NB,S-CU-CHD-B,S-CU-CHD-NB, S-CD-CHU-B,S-CD-CHU-NB,S-CD-CHD-B,S-CD-CHD-NB

            //check candle if up candle S-CU
            if (candle.getClose() - candle.getOpen() >= 0) {
                // check checkedCandle if up CHU
                if (checkedCandle.getClose() - checkedCandle.getOpen() >= 0) {
                    // check if low checkedCandle is below low of candle B
                    if (checkedCandle.getLow() < candle.getLow() && checkedCandle.getOpen() > candle.getOpen()){
                        candle.setSRType(SRType.S_CU_CHU_B);
                        supports.getSupportCandlesList().add(checkedCandle);
                    }
                    // check if low checkedCandle is above low of candle NB
                    if (checkedCandle.getLow() > candle.getLow() && checkedCandle.getLow() <= candle.getOpen()){
                        candle.setSRType(SRType.S_CU_CHU_NB);
                        supports.getSupportCandlesList().add(candle);
                    }
                }
                // check checkedCandle if down CHD
                if (checkedCandle.getClose() - checkedCandle.getOpen() < 0) {
                    // check if low checkedCandle is below low of candle B
                    if (checkedCandle.getLow() < candle.getLow() && checkedCandle.getClose() > candle.getOpen()){
                        candle.setSRType(SRType.S_CU_CHD_B);
                        supports.getSupportCandlesList().add(checkedCandle);
                    }
                    // check if low checkedCandle is above low of candle NB
                    if (checkedCandle.getLow() > candle.getLow() && checkedCandle.getLow() <= candle.getOpen()){
                        candle.setSRType(SRType.S_CU_CHD_NB);
                        supports.getSupportCandlesList().add(candle);
                    }
                }
            }

            //check candle if down candle S-CD
            if (candle.getClose() - candle.getOpen() < 0) {
                // check checkedCandle if up CHU
                if (checkedCandle.getClose() - checkedCandle.getOpen() >= 0) {
                    // check if low checkedCandle is below low of candle B
                    if (checkedCandle.getLow() < candle.getLow() && checkedCandle.getOpen() > candle.getClose()){
                        candle.setSRType(SRType.S_CD_CHU_B);
                        supports.getSupportCandlesList().add(checkedCandle);
                    }
                    // check if low checkedCandle is above low of candle NB
                    if (checkedCandle.getLow() > candle.getLow() && checkedCandle.getLow() <= candle.getClose()){
                        candle.setSRType(SRType.S_CD_CHU_NB);
                        supports.getSupportCandlesList().add(candle);
                    }
                }
                // check checkedCandle if down CHD
                if (checkedCandle.getClose() - checkedCandle.getOpen() < 0) {
                    // check if low checkedCandle is below low of candle B
                    if (checkedCandle.getLow() < candle.getLow() && checkedCandle.getClose() > candle.getClose()){
                        candle.setSRType(SRType.S_CD_CHD_B);
                        supports.getSupportCandlesList().add(checkedCandle);
                    }
                    // check if low checkedCandle is above low of candle NB
                    if (checkedCandle.getLow() > candle.getLow() && checkedCandle.getLow() <= candle.getClose()){
                        candle.setSRType(SRType.S_CD_CHD_NB);
                        supports.getSupportCandlesList().add(candle);
                    }
                }
            }

        }
    }
}
