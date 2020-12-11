import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
class Day1Test {

    @Test
    fun `test small input`() {
        // Given
        val input = "1721\n" +
                "979\n" +
                "366\n" +
                "299\n" +
                "675\n" +
                "1456"
        val day1 = Day1()

        // When
        val entriesThatSumTo2020: Triple<Int, Int, Int> = day1.getEntriesThatSumTo2020(input.lines())
        val actual = entriesThatSumTo2020.first + entriesThatSumTo2020.second + entriesThatSumTo2020.third
        println("first ${entriesThatSumTo2020}.first, second ${entriesThatSumTo2020.second}")
        println(entriesThatSumTo2020.first * entriesThatSumTo2020.second * entriesThatSumTo2020.third)

        // Then
        assertEquals( 2020, actual)
    }

    @Test
    fun `test complete input`() {
        // Given
        val input = "1587\n" +
                "1407\n" +
                "1717\n" +
                "1596\n" +
                "1566\n" +
                "1752\n" +
                "1925\n" +
                "1847\n" +
                "1716\n" +
                "1726\n" +
                "1611\n" +
                "1628\n" +
                "1853\n" +
                "1864\n" +
                "1831\n" +
                "1942\n" +
                "1634\n" +
                "1964\n" +
                "1603\n" +
                "1676\n" +
                "1256\n" +
                "1906\n" +
                "1655\n" +
                "1790\n" +
                "1666\n" +
                "1470\n" +
                "1540\n" +
                "1544\n" +
                "1100\n" +
                "1447\n" +
                "1384\n" +
                "1464\n" +
                "1651\n" +
                "1572\n" +
                "907\n" +
                "1653\n" +
                "1265\n" +
                "1510\n" +
                "1639\n" +
                "1818\n" +
                "376\n" +
                "1378\n" +
                "1132\n" +
                "1750\n" +
                "1491\n" +
                "1788\n" +
                "1882\n" +
                "1779\n" +
                "1640\n" +
                "1586\n" +
                "1525\n" +
                "1458\n" +
                "1994\n" +
                "1782\n" +
                "1412\n" +
                "1033\n" +
                "1416\n" +
                "1813\n" +
                "1520\n" +
                "1968\n" +
                "715\n" +
                "1396\n" +
                "1745\n" +
                "1506\n" +
                "1024\n" +
                "1798\n" +
                "1870\n" +
                "1615\n" +
                "1957\n" +
                "1718\n" +
                "1349\n" +
                "1983\n" +
                "1387\n" +
                "1738\n" +
                "1588\n" +
                "1321\n" +
                "1160\n" +
                "1907\n" +
                "1861\n" +
                "1940\n" +
                "1475\n" +
                "2004\n" +
                "1852\n" +
                "1760\n" +
                "1608\n" +
                "1028\n" +
                "1820\n" +
                "1495\n" +
                "1811\n" +
                "1737\n" +
                "1417\n" +
                "1316\n" +
                "1087\n" +
                "1803\n" +
                "1595\n" +
                "1346\n" +
                "1971\n" +
                "1692\n" +
                "1678\n" +
                "1330\n" +
                "1480\n" +
                "1097\n" +
                "1898\n" +
                "1973\n" +
                "1567\n" +
                "1733\n" +
                "1336\n" +
                "1381\n" +
                "1327\n" +
                "1670\n" +
                "1436\n" +
                "1989\n" +
                "1334\n" +
                "89\n" +
                "1862\n" +
                "1715\n" +
                "1743\n" +
                "1967\n" +
                "1765\n" +
                "1402\n" +
                "1729\n" +
                "1749\n" +
                "1671\n" +
                "1196\n" +
                "1650\n" +
                "1089\n" +
                "1814\n" +
                "1783\n" +
                "1225\n" +
                "1823\n" +
                "1746\n" +
                "2009\n" +
                "1886\n" +
                "1748\n" +
                "1481\n" +
                "1739\n" +
                "1912\n" +
                "1663\n" +
                "1668\n" +
                "1314\n" +
                "1594\n" +
                "705\n" +
                "1449\n" +
                "1731\n" +
                "1487\n" +
                "1648\n" +
                "1466\n" +
                "1317\n" +
                "1979\n" +
                "1799\n" +
                "1926\n" +
                "1703\n" +
                "1656\n" +
                "1978\n" +
                "2005\n" +
                "1865\n" +
                "1982\n" +
                "1951\n" +
                "1892\n" +
                "1713\n" +
                "1744\n" +
                "1598\n" +
                "1606\n" +
                "1583\n" +
                "1895\n" +
                "1804\n" +
                "1430\n" +
                "1816\n" +
                "1364\n" +
                "1575\n" +
                "1918\n" +
                "1431\n" +
                "1812\n" +
                "1471\n" +
                "1797\n" +
                "928\n" +
                "1934\n" +
                "1156\n" +
                "94\n" +
                "1563\n" +
                "1909\n" +
                "1453\n" +
                "1392\n" +
                "1427\n" +
                "1819\n" +
                "1524\n" +
                "1695\n" +
                "1866\n" +
                "2008\n" +
                "1413\n" +
                "1698\n" +
                "1051\n" +
                "1707\n" +
                "1904\n" +
                "1681\n" +
                "1541\n" +
                "1621\n" +
                "1421\n" +
                "1809\n" +
                "1576"
        val day1 = Day1()

        // When
        val entriesThatSumTo2020: Triple<Int, Int, Int> = day1.getEntriesThatSumTo2020(input.lines())
        val actual = entriesThatSumTo2020.first + entriesThatSumTo2020.second + entriesThatSumTo2020.third
        println("first $entriesThatSumTo2020.first, second entriesThatSumTo2020.second")
        println(entriesThatSumTo2020.first * entriesThatSumTo2020.second * entriesThatSumTo2020.third)

        // Then
        assertEquals( 2020, actual)
    }

}