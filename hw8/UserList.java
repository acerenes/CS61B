/* UserList.java */

import queue.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Random;

public class UserList {

    private CatenableQueue<User> userQueue;
    private int size;

    /**
    * Creates empty UserList containing no users
    **/
    public UserList(){
        userQueue = new CatenableQueue<User>();
        size = 0;
    }

    /**
    *  addUser() adds a defensive copy of the specified user.
    **/
    public void add(User u){
        if (u.getPagesPrinted() < 0) {
            System.out.println("A user cannot have a negative number of pages printed.");
            return;
        }
        User uCopy = new User(u.getId(), u.getPagesPrinted());
        userQueue.enqueue(uCopy);
        size++;
    }

    /**
    *  getSize() returns the number of users in the UserList
    **/
    public int getSize(){
        return size;
    }

    /**
    * getUsers() returns the CatenableQueue<User> of Users.
    **/
    public CatenableQueue<User> getUsers(){
        return userQueue;
    }

    /**
    *  toString() prints out the id and pages printed of all users in the UserList.
    **/
    public String toString(){
        return userQueue.toString();
    }

    /**
    *  partition() partitions qUnsorted using the pivot integer.  On completion of
    *  this method, qUnsorted is empty, and its items have been moved to qLess,
    *  qEqual, and qGreater, according to their relationship to the pivot.
    *
    *   @param sortFeature is a string that tells us what we are sorting. If we are
    *       sorting user IDs, sortFeatures equals "id". If we are sorting pages
    *       printed, sortFeatures equals "pages".
    *   @param qUnsorted is a CatenableQueue<User> of User objects.
    *   @param pivot is an integer used for partitioning.
    *   @param qSmall is a CatenableQueue<User>, in which all Users with sortFeature less than pivot
    *       will be enqueued.
    *   @param qEquals is a CatenableQueue<User>, in which all Users with sortFeature equal to the pivot
    *       will be enqueued.
    *   @param qLarge is a CatenableQueue<User>, in which all Users with sortFeature greater than pivot
    *       will be enqueued.  
    **/ 
    public static void partition(String sortFeature, CatenableQueue<User> qUnsorted, int pivot, 
        CatenableQueue<User> qLess, CatenableQueue<User> qEqual, CatenableQueue<User> qGreater) {

        /* if (pivot >= qUnsorted.size()) {
            return;
        } */

        if (sortFeature.equals("id")) {
            partitionID(pivot, qUnsorted, qLess, qEqual, qGreater);
        } else if (sortFeature.equals("pages")) {
            partitionPages(pivot, qUnsorted, qLess, qEqual, qGreater);
        } else {
            return;
        }
    }


    private static void partitionPages(int pivotValue, CatenableQueue<User> qUnsorted, CatenableQueue<User> qLess, CatenableQueue<User> qEqual, CatenableQueue<User> qGreater) {

        while (!qUnsorted.isEmpty()) {
            User currUser = qUnsorted.dequeue();
            if (currUser.getPagesPrinted() > pivotValue) {
                qGreater.enqueue(currUser);
            } else if (currUser.getPagesPrinted() < pivotValue) {
                qLess.enqueue(currUser);
            } else {
                qEqual.enqueue(currUser);
            }
        }
    }



    private static void partitionID(int pivotValue, CatenableQueue<User> qUnsorted, CatenableQueue<User> qLess, CatenableQueue<User> qEqual, CatenableQueue<User> qGreater) {

        while (!qUnsorted.isEmpty()) {
            User currUser = qUnsorted.dequeue();
            if (currUser.getId() > pivotValue) {
                qGreater.enqueue(currUser);
            } else if (currUser.getId() < pivotValue) {
                qLess.enqueue(currUser);
            } else {
                qEqual.enqueue(currUser);
            }
        }
    }



    /**
    *   quickSort() sorts q from smallest to largest according to sortFeature using quicksort.
    *   @param sortFeature is a string that tells us what we are sorting. If we are
    *       sorting user IDs, sortFeatures equals "id". If we are sorting pages
    *       printed, sortFeatures equals "pages".
    *   @param q is an unsorted CatenableQueue containing User items.
    **/
    public static void quickSort(String sortFeature, CatenableQueue<User> q){ 


        if (sortFeature.equals("id")) {
            quickSortID(q);
        } else if (sortFeature.equals("pages")) {
            quickSortPages(q);
        } else {
            return;
        }
            
    }

    private static void quickSortID(CatenableQueue<User> q) {

        if (q.size() <= 0) {
            return;
        }

        CatenableQueue<User> less = new CatenableQueue<User>();
        CatenableQueue<User> equal = new CatenableQueue<User>();
        CatenableQueue<User> greater = new CatenableQueue<User>();


        Random rand = new Random();
        int randomNum = rand.nextInt(q.size());

        User pivotUser = q.nth(randomNum);
        int pivotID = pivotUser.getId();

        partition("id", q, pivotID, less, equal, greater);

        // Do it again if not size 1, for less than and greater than. 

        if (less.size() > 1) {
            quickSortID(less);
        }
        if (greater.size() > 1) {
            quickSortID(greater);
        }

        // Partioning already dequeues it. 
        q.append(less);
        q.append(equal);
        q.append(greater);

    }

    private static void quickSortPages(CatenableQueue<User> q) {

        CatenableQueue<User> less = new CatenableQueue<User>();
        CatenableQueue<User> equal = new CatenableQueue<User>();
        CatenableQueue<User> greater = new CatenableQueue<User>();

        Random rand = new Random();
        int randomNum = rand.nextInt(q.size());

        User pivotUser = q.nth(randomNum);
        int pivotPages = pivotUser.getPagesPrinted();

        partition("pages", q, pivotPages, less, equal, greater);

        /* System.out.println("After partition: ");
        System.out.println("Less : " + less.toString());
        System.out.println("Equal: " + equal.toString());
        System.out.println("Great: " + greater.toString()); */

        // Do it again if not size 1, for less than and greater than. 

        if (less.size() > 1) {
            quickSortPages(less);
        }
        if (greater.size() > 1) {
            quickSortPages(greater);
        }

        // Partioning already dequeues it. 
        q.append(less);
        q.append(equal);
        q.append(greater);
        
    }

    /**
    *  quickSort() sorts userQueue from smallest to largest according to sortFeature, using quicksort.
    *  @param sortFeature is a string that equals "id" if we are sorting users by their IDs, or equals
    *  "pages" if we are sorting users by the number of pages they have printed.
    **/
    public void quickSort(String sortFeature){
        quickSort(sortFeature, userQueue);
    }


    /**
    *  makeQueueOfQueues() makes a queue of queues, each containing one User
    *  of userQueue.  Upon completion of this method, userQueue is empty.
    *  @return a CatenableQueue<CatenableQueue<User>>, where each CatenableQueue
    *    contains one User from userQueue.
    **/
    public CatenableQueue<CatenableQueue<User>> makeQueueOfQueues(){

        CatenableQueue<CatenableQueue<User>> metaQueue = new CatenableQueue<CatenableQueue<User>>();
        
        while (!this.userQueue.isEmpty()) {
            User currUser = this.userQueue.dequeue();
            CatenableQueue<User> innerQueue = new CatenableQueue<User>();
            innerQueue.enqueue(currUser);
            metaQueue.enqueue(innerQueue);
        }

        return metaQueue;
    }

    /**
    *  mergeTwoQueues() merges two sorted queues into one sorted queue.  On completion
    *  of this method, q1 and q2 are empty, and their Users have been merged
    *  into the returned queue. Assume q1 and q2 contain only User objects.
    *   @param sortFeature is a string that tells us what we are sorting. If we are
    *       sorting user IDs, sortFeatures equals "id". If we are sorting pages
    *       printed, sortFeatures equals "pages".
    *  @param q1 is CatenableQueue<User> of User objects, sorted from smallest to largest by their sortFeature.
    *  @param q2 is CatenableQueue<User> of User objects, sorted from smallest to largest by their sortFeature.
    *  @return a CatenableQueue<User> containing all the Users from q1 and q2 (and nothing else),
    *       sorted from smallest to largest by their sortFeature.
    **/
    public static CatenableQueue<User> mergeTwoQueues(String sortFeature, CatenableQueue<User> q1, CatenableQueue<User> q2){

        if (sortFeature.equals("id")) {
            return mergeTwoQueuesID(q1, q2);
        } else if (sortFeature.equals("pages")) {
            return mergeTwoQueuesPages(q1, q2);
        } else {
            return null;
        }
    }

    private static CatenableQueue<User> mergeTwoQueuesID (CatenableQueue<User> q1, CatenableQueue<User> q2) {

        CatenableQueue<User> sorted = new CatenableQueue<User>();

        while (!q1.isEmpty() && !q2.isEmpty()) {
            User one = q1.front();
            User two = q2.front();
            if (one.getId() <= two.getId()) {
                one = q1.dequeue();
                sorted.enqueue(one);
            } else {
                two = q2.dequeue();
                sorted.enqueue(two);
            }
        }

        // Leftovers:
        if (q1.isEmpty()) {
            sorted.append(q2);
        } else if (q2.isEmpty()) {
            sorted.append(q1);
        }

        return sorted;
    }

    private static CatenableQueue<User> mergeTwoQueuesPages (CatenableQueue<User> q1, CatenableQueue<User> q2) {

        CatenableQueue<User> sorted = new CatenableQueue<User>();

        while (!q1.isEmpty() && !q2.isEmpty()) {
            User one = q1.front();
            User two = q2.front();
            if (one.getPagesPrinted() <= two.getPagesPrinted()) {
                one = q1.dequeue();
                sorted.enqueue(one);
            } else {
                two = q2.dequeue();
                sorted.enqueue(two);
            }
        }

        // Leftovers:
        if (q1.isEmpty()) {
            sorted.append(q2);
        } else if (q2.isEmpty()) {
            sorted.append(q1);
        }

        return sorted;
    }

    /**
    *   mergeSort() sorts this UserList from smallest to largest according to sortFeature using mergesort.
    *   You should complete this method without writing any helper methods.
    *   @param sortFeature is a string that tells us what we are sorting. If we are
    *       sorting user IDs, sortFeatures equals "id". If we are sorting pages
    *       printed, sortFeatures equals "pages".
    **/
    public void mergeSort(String sortFeature){
        
        // Break up into N little pieces. 
        // Then merge into twice as bigs? , and put them into another metaqueue?
        // Keep going until 1 thing in the metaquee?
        // And then take out that queue?

        CatenableQueue<CatenableQueue<User>> metaQueue = this.makeQueueOfQueues();

        while (metaQueue.size() != 1) {
            CatenableQueue<CatenableQueue<User>> old = metaQueue;
            metaQueue = new CatenableQueue<CatenableQueue<User>>();
            int i = 0;
            while (i < old.size() - 1) {
                CatenableQueue<User> miniMerged = mergeTwoQueues(sortFeature, old.nth(i), old.nth(i + 1));
                metaQueue.enqueue(miniMerged);
                i = i + 2;   
            }

            if (i == old.size() - 1) {
                // Old #, make sure to put in the last one. 
                metaQueue.enqueue(old.nth(i));
            }
        }

        // So after here, metaQueue should just have one thing, so take it out. 
        this.userQueue = metaQueue.front();     
    }

    /**
    *   sortByBothFeatures() sorts this UserList's userQueue from smallest to largest pages printed.
    *   If two Users have printed the same number of pages, the User with the smaller user ID is first.
    **/
    public void sortByBothFeatures(){
        //Replace with solution. Don't overthink this one!

        // OH SORT BY ID THEN BY PAGES. OMG. SO BRILLIANT. 

        this.quickSort("id");
        this.mergeSort("pages");
    }


    @Test
    public void naivePartitionTest() {
        UserList list = new UserList();

        list.add(new User(0, 20));
        list.add(new User(1, 0));
        list.add(new User(2, 10));

        CatenableQueue<User> less = new CatenableQueue<User>();
        CatenableQueue<User> equal = new CatenableQueue<User>();
        CatenableQueue<User> greater = new CatenableQueue<User>();

        /* pivot on user 1 by id */
        list.partition("id", list.userQueue, 1, less, equal, greater);
        assertEquals(1, less.size());
        assertEquals(1, equal.size());
        assertEquals(1, greater.size());
        assertEquals(new User(0, 20), less.front());
        assertEquals(new User(1, 0), equal.front());
        assertEquals(new User(2, 10), greater.front());
        assertTrue(list.userQueue.isEmpty());
    }

    @Test
    public void partitionTest() {
        UserList list = new UserList();

        list.add(new User(0, 20));
        list.add(new User(1, 0));
        list.add(new User(2, 10));
        list.add(new User(3, 11));
        list.add(new User(4, 1));
        list.add(new User(5, 20));
        list.add(new User(6, 40));

        CatenableQueue<User> less = new CatenableQueue<User>();
        CatenableQueue<User> equal = new CatenableQueue<User>();
        CatenableQueue<User> greater = new CatenableQueue<User>();

        /* Pivot on user 0 by pages. */
        list.partition("pages", list.userQueue, 0, less, equal, greater);
        assertEquals(0, less.size());
        assertEquals(1, equal.size());
        assertEquals(6, greater.size());
        assertTrue(list.userQueue.isEmpty());
    } 

    @Test
    public void naiveQuickSortTest() {
        UserList list = new UserList();
        list.add(new User(2, 12));
        list.add(new User(0, 10));
        list.add(new User(1, 11));

        list.quickSort("id");

        String sorted =
         "[ User ID: 0, Pages Printed: 10,\n  User ID: 1, Pages Printed: 11,\n  User ID: 2, Pages Printed: 12 ]";

        assertEquals(sorted, list.toString());

        list.quickSort("pages");
        assertEquals(sorted, list.toString()); 
    }

    @Test
    public void quickSortTest() {
        UserList list = new UserList();
        list.add(new User(2, 12));
        list.add(new User(0, 10));
        list.add(new User(1, 11));
        list.add(new User(3, 1));
        list.add(new User(4, 1));

        list.quickSort("id");

        String sorted =
         "[ User ID: 0, Pages Printed: 10,\n  User ID: 1, Pages Printed: 11,\n  User ID: 2, Pages Printed: 12,\n  User ID: 3, Pages Printed: 1,\n  User ID: 4, Pages Printed: 1 ]";

        assertEquals(sorted, list.toString());

        list.quickSort("pages");

        String sortedPages = "[ User ID: 3, Pages Printed: 1,\n  User ID: 4, Pages Printed: 1,\n  User ID: 0, Pages Printed: 10,\n  User ID: 1, Pages Printed: 11,\n  User ID: 2, Pages Printed: 12 ]";
        assertEquals(sortedPages, list.toString()); 
    }

    @Test
    public void naiveMakeQueuesTest(){
        UserList list = new UserList();

        list.add(new User(0, 20));
        list.add(new User(1, 0));
        list.add(new User(2, 10));

        CatenableQueue<CatenableQueue<User>> queues = list.makeQueueOfQueues();
        String queueOfQueues = 
        "[ [ User ID: 0, Pages Printed: 20 ],\n  [ User ID: 1, Pages Printed: 0 ],\n  [ User ID: 2, Pages Printed: 10 ] ]";

        assertEquals(queueOfQueues, queues.toString());        
    }

    @Test
    public void naiveMergeQueuesTest(){
        CatenableQueue<User> q1 = new CatenableQueue<User>();
        CatenableQueue<User> q2 = new CatenableQueue<User>();
        q1.enqueue(new User(0, 20));
        q2.enqueue(new User(1, 10));

        CatenableQueue<User> merged = mergeTwoQueues("pages", q1, q2);
        String mergeByPages = 
        "[ User ID: 1, Pages Printed: 10,\n  User ID: 0, Pages Printed: 20 ]";
        assertEquals(mergeByPages, merged.toString());        

        q1 = new CatenableQueue<User>();
        q2 = new CatenableQueue<User>();
        q1.enqueue(new User(0, 20));
        q2.enqueue(new User(1, 10));

        merged = mergeTwoQueues("id", q1, q2);
        String mergeById = 
        "[ User ID: 0, Pages Printed: 20,\n  User ID: 1, Pages Printed: 10 ]";
        assertEquals(mergeById, merged.toString());        
    }

    @Test
    public void mergeQueuesTest() {
        CatenableQueue<User> q1 = new CatenableQueue<User>();
        CatenableQueue<User> q2 = new CatenableQueue<User>();

        q1.enqueue(new User(3, 1));
        q1.enqueue(new User(0, 20));
        q2.enqueue(new User(1, 10));
        q2.enqueue(new User(2, 11));

        CatenableQueue<User> merged = mergeTwoQueues("pages", q1, q2);
        String mergeByPages = 
        "[ User ID: 3, Pages Printed: 1,\n  User ID: 1, Pages Printed: 10,\n  User ID: 2, Pages Printed: 11,\n  User ID: 0, Pages Printed: 20 ]";
        assertEquals(mergeByPages, merged.toString());        

        q1 = new CatenableQueue<User>();
        q2 = new CatenableQueue<User>();
        q1.enqueue(new User(0, 20));
        q1.enqueue(new User(3, 1));
        q2.enqueue(new User(1, 10));
        q2.enqueue(new User(2, 11));

        merged = mergeTwoQueues("id", q1, q2);
        String mergeById = 
        "[ User ID: 0, Pages Printed: 20,\n  User ID: 1, Pages Printed: 10,\n  User ID: 2, Pages Printed: 11,\n  User ID: 3, Pages Printed: 1 ]";
        assertEquals(mergeById, merged.toString());   
    }

    @Test
    public void naiveMergeSortTest() {
        UserList list = new UserList();
        list.add(new User(2, 12));
        list.add(new User(0, 10));
        list.add(new User(1, 11));

        list.mergeSort("id");

        String sorted =
         "[ User ID: 0, Pages Printed: 10,\n  User ID: 1, Pages Printed: 11,\n  User ID: 2, Pages Printed: 12 ]";

        assertEquals(sorted, list.toString());

        list.mergeSort("pages");
        assertEquals(sorted, list.toString()); 
    }

    @Test
    public void mergeSortTest() {
        UserList list = new UserList();
        list.add(new User(2, 12));
        list.add(new User(0, 10));
        list.add(new User(1, 11));
        list.add(new User(3, 32));
        list.add(new User(4, 1));

        list.mergeSort("pages");

        String sorted =
         "[ User ID: 4, Pages Printed: 1,\n  User ID: 0, Pages Printed: 10,\n  User ID: 1, Pages Printed: 11,\n  User ID: 2, Pages Printed: 12,\n  User ID: 3, Pages Printed: 32 ]";

        assertEquals(sorted, list.toString());

        list.mergeSort("id");
        String sortedID = "[ User ID: 0, Pages Printed: 10,\n  User ID: 1, Pages Printed: 11,\n  User ID: 2, Pages Printed: 12,\n  User ID: 3, Pages Printed: 32,\n  User ID: 4, Pages Printed: 1 ]";
    }

    @Test
    public void naiveSortByBothTest() {
        UserList list = new UserList();
        list.add(new User(2, 12));
        list.add(new User(1, 10));
        list.add(new User(0, 10));

        list.sortByBothFeatures();

        String sorted =
         "[ User ID: 0, Pages Printed: 10,\n  User ID: 1, Pages Printed: 10,\n  User ID: 2, Pages Printed: 12 ]";

        assertEquals(sorted, list.toString());
    } 

    

    public static void main(String [] args) {

        // Uncomment the following line when ready
        jh61b.junit.textui.runClasses(UserList.class);
    }

}