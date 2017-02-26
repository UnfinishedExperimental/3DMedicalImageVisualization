import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Test2 {
	public static void main(String[] args)

    {

        Scanner keyboard = new Scanner(System.in);

        String keywordList = keyboard.nextLine();

        List<String> keywords = Arrays.asList(keywordList.split(" "));



        String numOfReviews = keyboard.nextLine();



        Map<String, Integer> hotelWords = new HashMap<String, Integer>();



        for (int index = 0; index < Integer.valueOf(numOfReviews); index++)

        {

            String hotelId = keyboard.nextLine();

            int number = 0;

            if (hotelWords.containsKey(hotelId))

            {

                number = hotelWords.get(hotelId);

            }



            String review = keyboard.nextLine();

            review.replace(".", "");

            review.replace(",", "");

            

            List<String> wordsInReview = Arrays.asList(review.split(" "));



            for (String word : wordsInReview)

            {

                if (keywords.contains(word))

                {

                    number++;

                }

            }

            hotelWords.put(hotelId, number);

        }

        keyboard.close();

        

        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(

            hotelWords.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()

        {

            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)

            {

                return (o2.getValue()).compareTo(o1.getValue());

            }

        });



        for (Map.Entry<String, Integer> hotel : list)

        {

            System.out.println(hotel.getKey());

        }

    }


}
