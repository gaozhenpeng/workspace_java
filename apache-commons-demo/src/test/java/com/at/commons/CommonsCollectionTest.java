package com.at.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Test;

public class CommonsCollectionTest {
    @Test
    public void testIsEmpty(){
        final List<String> aNullList = null;
        final List<String> anEmptyList = new ArrayList<String>();
        final List<String> aList_1ele = new ArrayList<String>();
        aList_1ele.add("aString");

        assertTrue(CollectionUtils.isEmpty(aNullList));
        assertTrue(CollectionUtils.isEmpty(anEmptyList));
        assertFalse(CollectionUtils.isEmpty(aList_1ele));
    }

    @Test
    public void testCollectionSort(){
        final List<String> aStrList = new ArrayList<String>();
        aStrList.add("123");
        aStrList.add("321");
        aStrList.add("1254567");
        aStrList.add("234432");
        aStrList.add("4567890");
        aStrList.add("668122");
        aStrList.add("337455");
        aStrList.add("ab9");
        aStrList.add("df0");
        Collections.sort(aStrList, new Comparator<String>(){

            @Override
            public int compare(final String a, final String b) {
                if(a == null || a.length() < 3){
                    if (b == null || b.length() < 3){
                        return 0; // equals
                    }else{
                        return -1; // b greater
                    }
                }else{
                    if(b == null || b.length() < 3){
                        return 1; // a greater
                    }else{
                        final String a_3 = a.substring(2, 3);
                        final String b_3 = b.substring(2, 3);
                        return a_3.compareTo(b_3);
                    }
                }
            }

        });

        assertEquals("Sorted String List is expected to be in order.", "[df0, 321, 123, 234432, 1254567, 4567890, 337455, 668122, ab9]", aStrList.toString());
    }


    private static class Account{
        private String name = null;
        private String address = null;
        private String telephone = null;

        private Account(){

        }

        public Account(final String name, final String address, final String telephone) {
            super();
            this.name = name;
            this.address = address;
            this.telephone = telephone;
        }

        public String getName() {
            return name;
        }
        public void setName(final String name) {
            this.name = name;
        }
        public String getAddress() {
            return address;
        }
        public void setAddress(final String address) {
            this.address = address;
        }
        public String getTelephone() {
            return telephone;
        }
        public void setTelephone(final String telephone) {
            this.telephone = telephone;
        }

    }

    private static class ComparableAccount implements Comparable<ComparableAccount>{
        private String name = null;
        private String address = null;
        private String telephone = null;

        private ComparableAccount(){

        }

        public ComparableAccount(final String name, final String address, final String telephone) {
            super();
            this.name = name;
            this.address = address;
            this.telephone = telephone;
        }

        public String getName() {
            return name;
        }
        public void setName(final String name) {
            this.name = name;
        }
        public String getAddress() {
            return address;
        }
        public void setAddress(final String address) {
            this.address = address;
        }
        public String getTelephone() {
            return telephone;
        }
        public void setTelephone(final String telephone) {
            this.telephone = telephone;
        }

        @Override
        public int compareTo(final ComparableAccount rhs) {
            if(rhs == null){
                return 1;
            }
            return new CompareToBuilder()
                .append(name, rhs.name)
                .append(address, rhs.address)
                .append(telephone, rhs.telephone)
                .toComparison();
        }

    }



    public int compareString(final String a, final String b){
        if(a == null && b == null){
            return 0;
        }else if(a != null && b == null){
            return -1;
        }else if(a == null && b != null){
            return 1;
        }
        return a.compareTo(b);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCollectionSort_chainedComparator(){
        final List<Account> accs = new ArrayList<Account>();
        accs.add(new Account("name1", "addr2", "tel3"));
        accs.add(new Account("name2", "addr1", "tel1"));
        accs.add(new Account("name1", "addr1", "tel1"));
        accs.add(new Account("name1", "addr1", "tel2"));
        accs.add(new Account("name1", "addr2", "tel1"));
        accs.add(new Account("name2", "addr2", "tel2"));
        accs.add(new Account("name2", "addr2", "tel3"));
        accs.add(new Account("name3", "addr1", "tel1"));
        accs.add(new Account("name1", "addr2", "tel2"));
        accs.add(new Account("name1", "addr1", "tel3"));
        accs.add(new Account("name3", "addr1", "tel2"));
        accs.add(new Account("name3", "addr1", "tel3"));
        accs.add(new Account("name2", "addr1", "tel2"));
        accs.add(new Account("name2", "addr1", "tel3"));
        accs.add(new Account("name2", "addr2", "tel1"));
        accs.add(new Account("name3", "addr2", "tel1"));
        accs.add(new Account("name3", "addr2", "tel2"));
        accs.add(new Account("name3", "addr2", "tel3"));

        final List<Account> expectedAccs = new LinkedList<Account>();
        expectedAccs.add(new Account("name1", "addr1", "tel1"));
        expectedAccs.add(new Account("name1", "addr1", "tel2"));
        expectedAccs.add(new Account("name1", "addr1", "tel3"));
        expectedAccs.add(new Account("name1", "addr2", "tel1"));
        expectedAccs.add(new Account("name1", "addr2", "tel2"));
        expectedAccs.add(new Account("name1", "addr2", "tel3"));
        expectedAccs.add(new Account("name2", "addr1", "tel1"));
        expectedAccs.add(new Account("name2", "addr1", "tel2"));
        expectedAccs.add(new Account("name2", "addr1", "tel3"));
        expectedAccs.add(new Account("name2", "addr2", "tel1"));
        expectedAccs.add(new Account("name2", "addr2", "tel2"));
        expectedAccs.add(new Account("name2", "addr2", "tel3"));
        expectedAccs.add(new Account("name3", "addr1", "tel1"));
        expectedAccs.add(new Account("name3", "addr1", "tel2"));
        expectedAccs.add(new Account("name3", "addr1", "tel3"));
        expectedAccs.add(new Account("name3", "addr2", "tel1"));
        expectedAccs.add(new Account("name3", "addr2", "tel2"));
        expectedAccs.add(new Account("name3", "addr2", "tel3"));

        Collections.sort(accs, ComparatorUtils.chainedComparator(
                new Comparator[]{
                        new Comparator<Account>(){
                            @Override
                            public int compare(final Account a, final Account b) {
                                if(a == null && b == null){
                                    return 0;
                                }else if(a != null && b == null){
                                    return -1;
                                }else if(a == null && b != null){
                                    return 1;
                                }
                                return compareString(a.getName(), b.getName());
                            }
                        },
                        new Comparator<Account>(){
                            @Override
                            public int compare(final Account a, final Account b) {
                                if(a == null && b == null){
                                    return 0;
                                }else if(a != null && b == null){
                                    return -1;
                                }else if(a == null && b != null){
                                    return 1;
                                }
                                return compareString(a.getAddress(), b.getAddress());
                            }
                        },
                        new Comparator<Account>(){
                            @Override
                            public int compare(final Account a, final Account b) {
                                if(a == null && b == null){
                                    return 0;
                                }else if(a != null && b == null){
                                    return -1;
                                }else if(a == null && b != null){
                                    return 1;
                                }
                                return compareString(a.getTelephone(), b.getTelephone());
                            }
                        },
                }
            )
        );

        for(int i = 0; i < expectedAccs.size(); i++){
            final Account sortedAcc = accs.get(i);
            final Account expectedAcc = expectedAccs.get(i);
            assertEquals("Name", expectedAcc.getName(), sortedAcc.getName());
            assertEquals("Address", expectedAcc.getAddress(), sortedAcc.getAddress());
            assertEquals("Telephone", expectedAcc.getTelephone(), sortedAcc.getTelephone());
        }

    }

    @Test
    public void testCollectionSort_AccountComparator(){
        final List<Account> accs = new ArrayList<Account>();
        accs.add(new Account("name1", "addr2", "tel3"));
        accs.add(new Account("name2", "addr1", "tel1"));
        accs.add(new Account("name1", "addr1", "tel1"));
        accs.add(new Account("name1", "addr1", "tel2"));
        accs.add(new Account("name1", "addr2", "tel1"));
        accs.add(new Account("name2", "addr2", "tel2"));
        accs.add(new Account("name2", "addr2", "tel3"));
        accs.add(new Account("name3", "addr1", "tel1"));
        accs.add(new Account("name1", "addr2", "tel2"));
        accs.add(new Account("name1", "addr1", "tel3"));
        accs.add(new Account("name3", "addr1", "tel2"));
        accs.add(new Account("name3", "addr1", "tel3"));
        accs.add(new Account("name2", "addr1", "tel2"));
        accs.add(new Account("name2", "addr1", "tel3"));
        accs.add(new Account("name2", "addr2", "tel1"));
        accs.add(new Account("name3", "addr2", "tel1"));
        accs.add(new Account("name3", "addr2", "tel2"));
        accs.add(new Account("name3", "addr2", "tel3"));

        final List<Account> expectedAccs = new LinkedList<Account>();
        expectedAccs.add(new Account("name1", "addr1", "tel1"));
        expectedAccs.add(new Account("name1", "addr1", "tel2"));
        expectedAccs.add(new Account("name1", "addr1", "tel3"));
        expectedAccs.add(new Account("name1", "addr2", "tel1"));
        expectedAccs.add(new Account("name1", "addr2", "tel2"));
        expectedAccs.add(new Account("name1", "addr2", "tel3"));
        expectedAccs.add(new Account("name2", "addr1", "tel1"));
        expectedAccs.add(new Account("name2", "addr1", "tel2"));
        expectedAccs.add(new Account("name2", "addr1", "tel3"));
        expectedAccs.add(new Account("name2", "addr2", "tel1"));
        expectedAccs.add(new Account("name2", "addr2", "tel2"));
        expectedAccs.add(new Account("name2", "addr2", "tel3"));
        expectedAccs.add(new Account("name3", "addr1", "tel1"));
        expectedAccs.add(new Account("name3", "addr1", "tel2"));
        expectedAccs.add(new Account("name3", "addr1", "tel3"));
        expectedAccs.add(new Account("name3", "addr2", "tel1"));
        expectedAccs.add(new Account("name3", "addr2", "tel2"));
        expectedAccs.add(new Account("name3", "addr2", "tel3"));

        Collections.sort(accs,
                new Comparator<Account>(){
                    public int compareString(final String a, final String b){
                        if(a == null && b == null){
                            return 0;
                        }else if(a != null && b == null){
                            return -1;
                        }else if(a == null && b != null){
                            return 1;
                        }
                        return a.compareTo(b);
                    }

                    @Override
                    public int compare(final Account a, final Account b) {
                        if(a == null && b == null){
                            return 0;
                        }else if(a != null && b == null){
                            return -1;
                        }else if(a == null && b != null){
                            return 1;
                        }
                        int val = -1;
                        val = compareString(a.getName(), b.getName());
                        if(val != 0){
                            return val;
                        }
                        val = compareString(a.getAddress(), b.getAddress());
                        if(val != 0){
                            return val;
                        }
                        val = compareString(a.getTelephone(), b.getTelephone());
                        return val;
                    }
                }
            );

        for(int i = 0; i < expectedAccs.size(); i++){
            final Account sortedAcc = accs.get(i);
            final Account expectedAcc = expectedAccs.get(i);
            assertEquals("Name", expectedAcc.getName(), sortedAcc.getName());
            assertEquals("Address", expectedAcc.getAddress(), sortedAcc.getAddress());
            assertEquals("Telephone", expectedAcc.getTelephone(), sortedAcc.getTelephone());
        }
    }

    @Test
    public void testCollectionSort_nullHighComparator(){
        final Comparator comparator = ComparatorUtils.nullHighComparator(ComparatorUtils.NATURAL_COMPARATOR);

        final List<ComparableAccount> accs = new ArrayList<ComparableAccount>();
        accs.add(new ComparableAccount("name1", "addr2", "tel3"));
        accs.add(new ComparableAccount("name2", "addr1", "tel1"));
        accs.add(new ComparableAccount("name1", "addr1", "tel1"));
        accs.add(new ComparableAccount("name1", "addr1", "tel2"));
        accs.add(new ComparableAccount("name1", "addr2", "tel1"));
        accs.add(new ComparableAccount("name2", "addr2", "tel2"));
        accs.add(new ComparableAccount("name2", "addr2", "tel3"));
        accs.add(new ComparableAccount("name3", "addr1", "tel1"));
        accs.add(new ComparableAccount("name1", "addr2", "tel2"));
        accs.add(new ComparableAccount("name1", "addr1", "tel3"));
        accs.add(new ComparableAccount("name3", "addr1", "tel2"));
        accs.add(new ComparableAccount("name3", "addr1", "tel3"));
        accs.add(new ComparableAccount("name2", "addr1", "tel2"));
        accs.add(new ComparableAccount("name2", "addr1", "tel3"));
        accs.add(new ComparableAccount("name2", "addr2", "tel1"));
        accs.add(new ComparableAccount("name3", "addr2", "tel1"));
        accs.add(new ComparableAccount("name3", "addr2", "tel2"));
        accs.add(new ComparableAccount("name3", "addr2", "tel3"));

        final List<ComparableAccount> expectedAccs = new LinkedList<ComparableAccount>();
        expectedAccs.add(new ComparableAccount("name1", "addr1", "tel1"));
        expectedAccs.add(new ComparableAccount("name1", "addr1", "tel2"));
        expectedAccs.add(new ComparableAccount("name1", "addr1", "tel3"));
        expectedAccs.add(new ComparableAccount("name1", "addr2", "tel1"));
        expectedAccs.add(new ComparableAccount("name1", "addr2", "tel2"));
        expectedAccs.add(new ComparableAccount("name1", "addr2", "tel3"));
        expectedAccs.add(new ComparableAccount("name2", "addr1", "tel1"));
        expectedAccs.add(new ComparableAccount("name2", "addr1", "tel2"));
        expectedAccs.add(new ComparableAccount("name2", "addr1", "tel3"));
        expectedAccs.add(new ComparableAccount("name2", "addr2", "tel1"));
        expectedAccs.add(new ComparableAccount("name2", "addr2", "tel2"));
        expectedAccs.add(new ComparableAccount("name2", "addr2", "tel3"));
        expectedAccs.add(new ComparableAccount("name3", "addr1", "tel1"));
        expectedAccs.add(new ComparableAccount("name3", "addr1", "tel2"));
        expectedAccs.add(new ComparableAccount("name3", "addr1", "tel3"));
        expectedAccs.add(new ComparableAccount("name3", "addr2", "tel1"));
        expectedAccs.add(new ComparableAccount("name3", "addr2", "tel2"));
        expectedAccs.add(new ComparableAccount("name3", "addr2", "tel3"));

        Collections.sort(accs, comparator );

        for(int i = 0; i < expectedAccs.size(); i++){
            final ComparableAccount sortedAcc = accs.get(i);
            final ComparableAccount expectedAcc = expectedAccs.get(i);
            assertEquals("Name", expectedAcc.getName(), sortedAcc.getName());
            assertEquals("Address", expectedAcc.getAddress(), sortedAcc.getAddress());
            assertEquals("Telephone", expectedAcc.getTelephone(), sortedAcc.getTelephone());
        }
    }


    private static class ComparetoReturn0Account implements Comparable<ComparetoReturn0Account>{
        private String name = null;
        private String address = null;
        private String telephone = null;

        private ComparetoReturn0Account(){

        }

        public ComparetoReturn0Account(final String name, final String address, final String telephone) {
            super();
            this.name = name;
            this.address = address;
            this.telephone = telephone;
        }

        public String getName() {
            return name;
        }
        public void setName(final String name) {
            this.name = name;
        }
        public String getAddress() {
            return address;
        }
        public void setAddress(final String address) {
            this.address = address;
        }
        public String getTelephone() {
            return telephone;
        }
        public void setTelephone(final String telephone) {
            this.telephone = telephone;
        }

        @Override
        public int compareTo(final ComparetoReturn0Account rhs) {
            return 0; // have to be 0;
        }

    }
    @Test
    public void testCollectionSort_transformedComparator(){
        final Comparator nullHighNaturalComparator = ComparatorUtils.nullHighComparator(ComparatorUtils.NATURAL_COMPARATOR);
        final Comparator nameComparator = ComparatorUtils.transformedComparator(nullHighNaturalComparator, new Transformer(){
            @Override
            public Object transform(final Object input) {
                return ((ComparetoReturn0Account)input).getName();
            }

        });
        final Comparator addrComparator = ComparatorUtils.transformedComparator(nullHighNaturalComparator, new Transformer(){
            @Override
            public Object transform(final Object input) {
                return ((ComparetoReturn0Account)input).getAddress();
            }

        });
        final Comparator telComparator = ComparatorUtils.transformedComparator(nullHighNaturalComparator, new Transformer(){
            @Override
            public Object transform(final Object input) {
                return ((ComparetoReturn0Account)input).getTelephone();
            }

        });

        final Comparator allComparator = ComparatorUtils.chainedComparator(new Comparator[]{nullHighNaturalComparator, nameComparator, addrComparator, telComparator});

        final List<ComparetoReturn0Account> accs = new ArrayList<ComparetoReturn0Account>();
        accs.add(new ComparetoReturn0Account("name1", "addr2", "tel3"));
        accs.add(new ComparetoReturn0Account("name2", "addr1", "tel1"));
        accs.add(new ComparetoReturn0Account("name1", "addr1", "tel1"));
        accs.add(new ComparetoReturn0Account("name1", "addr1", "tel2"));
        accs.add(new ComparetoReturn0Account("name1", "addr2", "tel1"));
        accs.add(new ComparetoReturn0Account("name2", "addr2", "tel2"));
        accs.add(new ComparetoReturn0Account("name2", "addr2", "tel3"));
        accs.add(new ComparetoReturn0Account("name3", "addr1", "tel1"));
        accs.add(new ComparetoReturn0Account("name1", "addr2", "tel2"));
        accs.add(new ComparetoReturn0Account("name1", "addr1", "tel3"));
        accs.add(new ComparetoReturn0Account("name3", "addr1", "tel2"));
        accs.add(new ComparetoReturn0Account("name3", "addr1", "tel3"));
        accs.add(new ComparetoReturn0Account("name2", "addr1", "tel2"));
        accs.add(new ComparetoReturn0Account("name2", "addr1", "tel3"));
        accs.add(new ComparetoReturn0Account("name2", "addr2", "tel1"));
        accs.add(new ComparetoReturn0Account("name3", "addr2", "tel1"));
        accs.add(new ComparetoReturn0Account("name3", "addr2", "tel2"));
        accs.add(new ComparetoReturn0Account("name3", "addr2", "tel3"));

        final List<ComparetoReturn0Account> expectedAccs = new LinkedList<ComparetoReturn0Account>();
        expectedAccs.add(new ComparetoReturn0Account("name1", "addr1", "tel1"));
        expectedAccs.add(new ComparetoReturn0Account("name1", "addr1", "tel2"));
        expectedAccs.add(new ComparetoReturn0Account("name1", "addr1", "tel3"));
        expectedAccs.add(new ComparetoReturn0Account("name1", "addr2", "tel1"));
        expectedAccs.add(new ComparetoReturn0Account("name1", "addr2", "tel2"));
        expectedAccs.add(new ComparetoReturn0Account("name1", "addr2", "tel3"));
        expectedAccs.add(new ComparetoReturn0Account("name2", "addr1", "tel1"));
        expectedAccs.add(new ComparetoReturn0Account("name2", "addr1", "tel2"));
        expectedAccs.add(new ComparetoReturn0Account("name2", "addr1", "tel3"));
        expectedAccs.add(new ComparetoReturn0Account("name2", "addr2", "tel1"));
        expectedAccs.add(new ComparetoReturn0Account("name2", "addr2", "tel2"));
        expectedAccs.add(new ComparetoReturn0Account("name2", "addr2", "tel3"));
        expectedAccs.add(new ComparetoReturn0Account("name3", "addr1", "tel1"));
        expectedAccs.add(new ComparetoReturn0Account("name3", "addr1", "tel2"));
        expectedAccs.add(new ComparetoReturn0Account("name3", "addr1", "tel3"));
        expectedAccs.add(new ComparetoReturn0Account("name3", "addr2", "tel1"));
        expectedAccs.add(new ComparetoReturn0Account("name3", "addr2", "tel2"));
        expectedAccs.add(new ComparetoReturn0Account("name3", "addr2", "tel3"));

        Collections.sort(accs, allComparator );

        for(int i = 0; i < expectedAccs.size(); i++){
            final ComparetoReturn0Account sortedAcc = accs.get(i);
            final ComparetoReturn0Account expectedAcc = expectedAccs.get(i);
            assertEquals("Name", expectedAcc.getName(), sortedAcc.getName());
            assertEquals("Address", expectedAcc.getAddress(), sortedAcc.getAddress());
            assertEquals("Telephone", expectedAcc.getTelephone(), sortedAcc.getTelephone());
        }
    }
}