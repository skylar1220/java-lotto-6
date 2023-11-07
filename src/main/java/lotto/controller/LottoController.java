package lotto.controller;

import static lotto.util.RetryUtil.read;

import java.util.Arrays;
import java.util.List;
import lotto.domain.Lotto;
import lotto.domain.LottoCollection;
import lotto.domain.LottoCollectionGenerator;
import lotto.domain.LottoCount;
import lotto.domain.MatchingCase;
import lotto.domain.NumberGenerator;
import lotto.domain.Profit;
import lotto.domain.Purchase;
import lotto.dto.WinningNumbersDto;
import lotto.util.RetryUtil;
import lotto.view.InputView;
import lotto.view.OutputView;

public class LottoController {
    private final NumberGenerator numberGenerator;

    public LottoController(NumberGenerator numberGenerator) {
        this.numberGenerator = numberGenerator;
    }

    public void run() {
        Purchase purchase = getPurchase();
        LottoCount lottoCount = getLottoCount(purchase);
        OutputView.printLottoCount(lottoCount.getCount());

        LottoCollection lottoCollection = getLottoCollection(lottoCount);
        OutputView.printLottoCollection(lottoCollection.getLottoCollection());

        List<MatchingCase> matchingResult = getMatchingResult(lottoCollection);
        Profit profit = getProfit(purchase);

        announceResult(matchingResult, profit);
    }

    private Purchase getPurchase() {
        int rawPurchase = read(InputView::inputPurchase);
        return Purchase.from(rawPurchase);
    }

    private LottoCount getLottoCount(Purchase purchase) {
        return LottoCount.from(purchase.getPurchase());
    }

    private LottoCollection getLottoCollection(LottoCount lottoCount) {
        LottoCollectionGenerator lottoCollectionGenerator = new LottoCollectionGenerator(lottoCount.getCount(), numberGenerator);
        return LottoCollection.from(lottoCollectionGenerator.generate());
    }

    private List<MatchingCase> getMatchingResult(LottoCollection lottoCollection) {
        WinningNumbersDto winningNumbersDto = read(InputView::inputWinningNumbers);
        Lotto winningLotto = new Lotto(winningNumbersDto.getWinningNumbers());
        int bonusNumber = read(InputView::inputBonusNumber);
        MatchingCase.INIT.initMathcingCase();
        lottoCollection.setResultGroup(winningLotto, bonusNumber);
        return MatchingCase.INIT.getValues();
        // 네이밍 , 호출 상수 개선 필요
    }

    private Profit getProfit(Purchase purchase) {
        return Profit.from(purchase.getPurchase());
    }

    private void announceResult(List<MatchingCase> matchingResult, Profit profit) {
        OutputView.printResult(matchingResult);
        OutputView.printProfit(profit.getProfit());
    }
}