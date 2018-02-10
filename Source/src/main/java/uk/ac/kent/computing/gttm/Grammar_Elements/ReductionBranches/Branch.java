
package uk.ac.kent.computing.gttm.Grammar_Elements.ReductionBranches;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.ac.kent.computing.gttm.Grammar_Elements.ExceptionClasses.BranchingWellFormednessException;
import uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure.Beat;

/**A class that describes a generic branch object that forms a branching tree
 * hierarchy
 *
 * @author Alexander Dodd
 */
public abstract class Branch implements Serializable{
    
    private final Beat pitchEvent;
    private final int level;
    private final Map<Integer, Branch> childBranches;
    private Branch parent = null;
    
    /**
     * 
     * @param pitchEvent the Beat object associated with this branch
     * @param level the level at which this branch branches from its parent. A value of
     * 0 indicates that this is the top level branch of the structure, and progressively lower numbers
     * indicate progressively less hierarchically significant branches.
     * 
     */
    public Branch(Beat pitchEvent, int level)
    {
        this.pitchEvent = pitchEvent;
        this.level = level;
        
        childBranches = new HashMap<>();
    }
    
    /**
     * Adds a branch as a child of this branch object.
     * @param branch the branch to be added as the child of this branch.
     * @throws BranchingWellFormednessException Indicates that adding the branch as a
     * child of this branching object would violate a well formedness constraint.
     */
    public void addChildBranch(Branch branch) 
            throws BranchingWellFormednessException
    {
        //check child branch is not at a higher or equal level to this branch object
        if(level >= branch.getLevel())
        {
            throw new BranchingWellFormednessException("Exception attempting to "
                    + "add a child branch that exists at a lower level");
        }
               
        //check there is not already a child branch of the level of the proposed sub branch
        if(childBranches.containsKey(branch.getLevel()))
        {
             throw new BranchingWellFormednessException("Exception attempting to "
                    + "add two or more child branch that branches at same level");
        }
        
        childBranches.put(branch.getLevel(), branch);
        branch.setParent(this);
        
    }
    
    /**
     * 
     * @param parent sets a Branch object as the parent of this branch object
     * if a parent is not already established.
     */
    protected void setParent(Branch parent)
    {
        if(this.parent == null)
        {
            this.parent = parent;
        }
    }
    
    /**
     * 
     * @return the level at which this branch branches from its parent branch. A value of
     * 0 indicates that this is a top level branch.
     */
    public int getLevel()
    {
        return level;
    }
    
    /**
     * 
     * @return the Beat object associated with this branch
     */
    public Beat getAssociatedBeat()
    {
        return pitchEvent;
    }
    
    /**
     * 
     * @return an unmodifiable map that describes the child branches associated 
     * with this branch object and their associated levels.
     */
    public Map<Integer, Branch> getChildBranches()
    {
        return Collections.unmodifiableMap(childBranches);
    }

    public Branch getParent() {
        return parent;
    }
    
    public List<Integer> getOrderedLevels()
    {
        List<Integer> levels = new ArrayList<>();

        levels.addAll(getChildBranches().keySet());
        Collections.sort(levels);
        
        return levels;
    }
    
    public List<Branch> getAllSubBranches()
    {
       List<Branch> subBranches = new ArrayList<>();
       addSubBranches(this, subBranches);
       
       return subBranches;        
        
    }
    
    
    private void addSubBranches(Branch branch, List<Branch> branches)
    {
        for (Integer child : branch.getOrderedLevels()) {
            
            Branch childBranch = branch.getChildBranches().get(child); 
            branches.add(childBranch);
            addSubBranches(childBranch, branches);
        }

      
    }
   
    
}
